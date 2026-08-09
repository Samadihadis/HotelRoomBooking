package com.samadihadis.hotelroombooking.service;

import com.samadihadis.hotelroombooking.entity.Booking;
import com.samadihadis.hotelroombooking.entity.Room;
import com.samadihadis.hotelroombooking.entity.User;
import com.samadihadis.hotelroombooking.enumes.BookingState;
import com.samadihadis.hotelroombooking.enumes.RoomState;
import com.samadihadis.hotelroombooking.enumes.UserState;
import com.samadihadis.hotelroombooking.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final UserService userService;


    @Transactional
    public Booking createBooking(Booking booking) {
        validateBooking(booking);

        Room room = roomService.getRoomById(booking.getRoom().getId());
        User user = userService.findUserById(booking.getUser().getId());

        if (room.getRoomState() != RoomState.AVAILABLE) {
            throw new RuntimeException("اتاق مورد نظر در حال حاضر قابل رزرو نیست.");
        }

        if (user.getUserState() != UserState.ACTIVE) {
            throw new RuntimeException("کاربر غیرفعال است و نمی‌تواند رزرو کند.");
        }

        if (booking.getGuestCount() > room.getMaxCapacity()) {
            throw new RuntimeException("تعداد مهمان‌ها بیشتر از ظرفیت اتاق است.");
        }

        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                room.getId(),
                booking.getCheckinDate(),
                booking.getCheckoutDate()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("اتاق در تاریخ‌های انتخاب شده رزرو است.");
        }

        long nights = java.time.temporal.ChronoUnit.DAYS.between(
                booking.getCheckinDate(),
                booking.getCheckoutDate()
        );

        if (nights <= 0) {
            throw new RuntimeException("تاریخ خروج باید بعد از تاریخ ورود باشد.");
        }

        BigDecimal totalPrice = room.getBasePrice().multiply(BigDecimal.valueOf(nights));
        booking.setTotalPrice(totalPrice);

        booking.setBookingState(BookingState.PENDING);
        booking.setReserveDate(LocalDate.now());

        Booking savedBooking = bookingRepository.save(booking);

        roomService.updateRoomState(room.getId(), RoomState.RESERVED);

        return savedBooking;
    }


    public Booking findBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("رزرو با شناسه %d یافت نشد.", id)
                ));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = findBookingById(id);
        if (booking.getBookingState() != BookingState.CANCELLED &&
                booking.getBookingState() != BookingState.CHECKOUT) {
            throw new RuntimeException("امکان حذف رزروهای فعال وجود ندارد.");
        }
        bookingRepository.deleteById(id);
    }

    public List<Booking> findBookingsByState(BookingState bookingState) {
        return bookingRepository.findByBookingState(bookingState);
    }

    public List<Booking> findBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> findBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    public List<Booking> getConflictingBookings(Long roomId, LocalDate checkinDate,
                                                LocalDate checkoutDate) {
        return bookingRepository.findConflictingBookings(roomId, checkinDate, checkoutDate);
    }

    public List<Booking> getBookingsByDateRange(LocalDate start, LocalDate end) {
        return bookingRepository.findByCheckinDateBetweenOrCheckoutDateBetween(start, end, start, end);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = findBookingById(bookingId);

        if (booking.getBookingState() == BookingState.CHECKIN ||
                booking.getBookingState() == BookingState.CHECKOUT) {
            throw new RuntimeException("امکان کنسل کردن رزرو پس از چک‌این وجود ندارد.");
        }

        booking.setBookingState(BookingState.CANCELLED);
        roomService.updateRoomState(booking.getRoom().getId(), RoomState.AVAILABLE);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking checkIn(Long bookingId) {
        Booking booking = findBookingById(bookingId);

        if (booking.getBookingState() != BookingState.PENDING) {
            throw new RuntimeException("فقط رزروهای در انتظار قابل چک‌این هستند.");
        }

        if (!LocalDate.now().equals(booking.getCheckinDate())) {
            throw new RuntimeException("تاریخ امروز با تاریخ چک‌این مطابقت ندارد.");
        }

        booking.setBookingState(BookingState.CHECKIN);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking checkOut(Long bookingId) {
        Booking booking = findBookingById(bookingId);

        if (booking.getBookingState() != BookingState.CHECKIN) {
            throw new RuntimeException("فقط رزروهای چک‌این شده قابل چک‌آوت هستند.");
        }

        booking.setBookingState(BookingState.CHECKOUT);
        roomService.updateRoomState(booking.getRoom().getId(), RoomState.AVAILABLE);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateBooking(Long bookingId, LocalDate newCheckin, LocalDate newCheckout) {
        Booking booking = findBookingById(bookingId);

        if (booking.getBookingState() != BookingState.PENDING) {
            throw new RuntimeException("فقط رزروهای در انتظار قابل تغییر هستند.");
        }

        if (newCheckin.isBefore(LocalDate.now())) {
            throw new RuntimeException("تاریخ ورود نمی‌تواند قبل از امروز باشد.");
        }

        if (newCheckout.isBefore(newCheckin)) {
            throw new RuntimeException("تاریخ خروج باید بعد از تاریخ ورود باشد.");
        }

        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                booking.getRoom().getId(), newCheckin, newCheckout
        );

        conflicts.removeIf(b -> b.getId().equals(bookingId));

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("اتاق در تاریخ‌های جدید رزرو است.");
        }

        booking.setCheckinDate(newCheckin);
        booking.setCheckoutDate(newCheckout);

        long nights = ChronoUnit.DAYS.between(newCheckin, newCheckout);
        BigDecimal newPrice = booking.getRoom().getBasePrice().multiply(BigDecimal.valueOf(nights));
        booking.setTotalPrice(newPrice);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateBookingState(Long bookingId, BookingState newState) {
        Booking booking = findBookingById(bookingId);
        booking.setBookingState(newState);
        return bookingRepository.save(booking);
    }

    private void validateBooking(Booking booking) {

        if (booking.getCheckinDate() == null || booking.getCheckoutDate() == null) {
            throw new RuntimeException("تاریخ ورود و خروج الزامی است.");
        }

        if (booking.getCheckinDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("تاریخ ورود نمی‌تواند قبل از امروز باشد.");
        }

        if (booking.getCheckoutDate().isBefore(booking.getCheckinDate())) {
            throw new RuntimeException("تاریخ خروج باید بعد از تاریخ ورود باشد.");
        }

        if (booking.getGuestCount() == null || booking.getGuestCount() <= 0) {
            throw new RuntimeException("تعداد مهمان‌ها باید بیشتر از صفر باشد.");
        }
    }
}
