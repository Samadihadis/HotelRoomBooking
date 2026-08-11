package com.samadihadis.hotelroombooking.service;

import com.samadihadis.hotelroombooking.dto.BookingCreateRequest;
import com.samadihadis.hotelroombooking.dto.BookingResponse;
import com.samadihadis.hotelroombooking.entity.Booking;
import com.samadihadis.hotelroombooking.entity.Room;
import com.samadihadis.hotelroombooking.entity.User;
import com.samadihadis.hotelroombooking.enumes.BookingState;
import com.samadihadis.hotelroombooking.mapper.BookingMapper;
import com.samadihadis.hotelroombooking.repository.BookingRepository;
import com.samadihadis.hotelroombooking.repository.RoomRepository;
import com.samadihadis.hotelroombooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request) {
        validateDates(request.getCheckinDate(), request.getCheckoutDate());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("کاربر یافت نشد."));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("اتاق یافت نشد."));

        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                request.getRoomId(), request.getCheckinDate(), request.getCheckoutDate());

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("این اتاق در بازه زمانی انتخاب‌شده قبلاً رزرو شده است.");
        }

        Booking booking = bookingMapper.toEntity(request);
        booking.setUser(user);
        booking.setRoom(room);
        booking.setBookingState(BookingState.PENDING); // یا CONFIRMED
        booking.setTotalPrice(calculateTotalPrice(room, request.getCheckinDate(), request.getCheckoutDate()));

        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public BookingResponse findBookingById(Long id) {
        return bookingMapper.toResponse(findBookingEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = findBookingEntityById(id);
        if (booking.getBookingState() == BookingState.CHECKIN) {
            throw new RuntimeException("رزرو در حال اقامت قابل حذف نیست.");
        }
        bookingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> findBookingsByState(BookingState state) {
        return bookingRepository.findByBookingState(state)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> findBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> findBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getConflictingBookings(Long roomId, LocalDate checkin, LocalDate checkout) {
        return bookingRepository.findConflictingBookings(roomId, checkin, checkout)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByDateRange(LocalDate start, LocalDate end) {
        return bookingRepository.findByCheckinDateBetweenOrCheckoutDateBetween(start, end, start, end)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Transactional
    public BookingResponse cancelBooking(Long id) {
        Booking booking = findBookingEntityById(id);

        if (booking.getBookingState() == BookingState.CHECKIN ||
                booking.getBookingState() == BookingState.CHECKOUT) {
            throw new RuntimeException("امکان لغو این رزرو وجود ندارد.");
        }

        booking.setBookingState(BookingState.CANCELLED);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse checkIn(Long id) {
        Booking booking = findBookingEntityById(id);

        if (booking.getBookingState() != BookingState.CONFIRMED &&
                booking.getBookingState() != BookingState.PENDING) {
            throw new RuntimeException("فقط رزروهای تأیید شده قابل چک‌این هستند.");
        }

        booking.setBookingState(BookingState.CHECKIN);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse checkOut(Long id) {
        Booking booking = findBookingEntityById(id);

        if (booking.getBookingState() != BookingState.CHECKIN) {
            throw new RuntimeException("فقط رزروهای چک‌این شده قابل چک‌اوت هستند.");
        }

        booking.setBookingState(BookingState.CHECKOUT);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse updateBooking(Long id, LocalDate checkinDate, LocalDate checkoutDate) {
        validateDates(checkinDate, checkoutDate);

        Booking booking = findBookingEntityById(id);

        // چک تداخل (به جز خود این رزرو)
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                booking.getRoom().getId(), checkinDate, checkoutDate);

        boolean hasConflict = conflicts.stream()
                .anyMatch(b -> !b.getId().equals(id));

        if (hasConflict) {
            throw new RuntimeException("در این بازه زمانی تداخل وجود دارد.");
        }

        booking.setCheckinDate(checkinDate);
        booking.setCheckoutDate(checkoutDate);
        booking.setTotalPrice(calculateTotalPrice(booking.getRoom(), checkinDate, checkoutDate));

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse updateBookingState(Long id, BookingState newState) {
        Booking booking = findBookingEntityById(id);
        booking.setBookingState(newState);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }


    private Booking findBookingEntityById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("رزرو با شناسه %d یافت نشد.", id)
                ));
    }

    private void validateDates(LocalDate checkin, LocalDate checkout) {
        if (checkout.isBefore(checkin) || checkout.isEqual(checkin)) {
            throw new RuntimeException("تاریخ خروج باید بعد از تاریخ ورود باشد.");
        }
    }

    private BigDecimal calculateTotalPrice(Room room, LocalDate checkin, LocalDate checkout) {
        long nights = ChronoUnit.DAYS.between(checkin, checkout);
        return room.getBasePrice().multiply(BigDecimal.valueOf(nights));
    }
}