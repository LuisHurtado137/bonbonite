package org.luis.models;

public record BillingInfo(
        String documentType,
        String documentNumber,
        String firstName,
        String lastName,
        String gender,
        String email,
        String phone,
        String country,
        String state,
        String city,
        String address) {

    public static BillingInfo sample(String email, String documentNumber) {
        return new BillingInfo(
                "Cédula de ciudadanía",   // TODO: usa el texto exacto de la opción en el sitio
                documentNumber,
                "Luis",
                "QA",
                "Hombre",
                email,
                "3000000000",
                "Colombia",
                "Antioquia",
                "Medellín",
                "Calle 1 # 2-3");
    }
}