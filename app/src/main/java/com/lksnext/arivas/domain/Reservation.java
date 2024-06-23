package com.lksnext.arivas.domain;

public class Reservation {
    private String uid;         // UID del usuario que realiza la reserva
    private String slotId;      // ID del slot reservado
    private String date;        // Fecha de la reserva (formato a definir)
    private String in;          // Hora de entrada (ej. "10:00")
    private String out;         // Hora de salida (ej. "12:00")

    public Reservation() {
        // Constructor vacío requerido para Firebase
    }

    public Reservation(String uid, String slotId, String date, String in, String out) {
        this.uid = uid;
        this.slotId = slotId;
        this.date = date;
        this.in = in;
        this.out = out;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }
}
/*
* // Obtén una referencia a tu colección en Firestore
FirebaseFirestore db = FirebaseFirestore.getInstance();
CollectionReference reservationsRef = db.collection("reservations");

// Crea un objeto de reserva
String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // UID del usuario actualmente autenticado
String slotId = "slot123"; // Ejemplo de ID de slot (puedes cambiar esto según tu lógica)
String date = "2024-06-21"; // Fecha de la reserva (ejemplo, asegúrate de usar un formato apropiado)
String inTime = "10:00"; // Hora de entrada
String outTime = "12:00"; // Hora de salida

Reservation reservation = new Reservation(uid, slotId, date, inTime, outTime);

// Agrega la reserva a Firestore
reservationsRef.add(reservation)
        .addOnSuccessListener(documentReference -> {
            Log.d(TAG, "Reserva agregada con ID: " + documentReference.getId());
            // Aquí puedes realizar cualquier acción adicional después de agregar la reserva
        })
        .addOnFailureListener(e -> {
            Log.w(TAG, "Error al agregar la reserva", e);
            // Maneja errores si es necesario
        });

* */