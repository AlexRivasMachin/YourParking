package com.lksnext.arivas.data;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;

public class FirestoreSetup {
    private FirebaseFirestore db;

    public FirestoreSetup() {
        db = FirebaseFirestore.getInstance();
    }

    public void populateParkingSlots() {
        String[] tipos = {"STD", "ELEC", "MOTO", "DISC"};
        for (int i = 1; i <= 100; i++) {
            Map<String, Object> slot = new HashMap<>();
            slot.put("ID", i);
            slot.put("TIPO", tipos[i % tipos.length]);
            db.collection("parking_slots").document(String.valueOf(i))
                    .set(slot);
        }
    }
}
