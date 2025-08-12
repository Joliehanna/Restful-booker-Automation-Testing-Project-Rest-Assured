package UpdateBooking;

public class Data {
    public static booking Body()
    {
        booking b = new booking();
        b.setFirstname("Jolie");
        b.setLastname("Hanna");
        b.setDepositpaid(true);
        b.setAdditionalneeds("Breakfast & dinner");
        b.setTotalprice(250);

        bookingdates d =new bookingdates();
        d.setCheckin("2024-09-09");
        d.setCheckout("2024-09-16");
        b.setBookingdates(d);
        return b;
    }
}
