package POJO;

public class Data {
    public static booking Body()
    {
        booking B = new booking();
        B.setFirstname("Jolie");
        B.setLastname("Adel");
        B.setDepositpaid(true);
        B.setAdditionalneeds("Breakfast & dinner");
        B.setTotalprice(299);

        bookingdates d =new bookingdates();
        d.setCheckin("2024-09-09");
        d.setCheckout("2024-09-16");
        B.setBookingdates(d);
        return B;
    }
}
