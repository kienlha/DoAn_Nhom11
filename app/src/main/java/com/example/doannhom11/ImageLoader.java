package com.example.doannhom11;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ImageLoader {
    public static void Load(String URL, ImageView view)
    {
        StorageReference path = FirebaseStorage.getInstance().getReference().child(URL);
        path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(view);
            }
        });
    }
    public static void Upload(String URL, ImageView view)
    {
        Bitmap image = ((RoundedDrawable) view.getDrawable()).getSourceBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask task = FirebaseStorage.getInstance().getReference().child(URL).putBytes(data);
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // handle unsuccessful uploads
            }
        });
    }
}
