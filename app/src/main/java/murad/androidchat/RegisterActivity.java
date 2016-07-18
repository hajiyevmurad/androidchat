package murad.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 17.07.2016.
 */
public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    protected EditText passwordEditText;
    protected EditText nameEditText;
    protected EditText emailEditText;
    protected Button registerButton;

    private DatabaseReference mDatabase;

    private static final String TAG = RegisterActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = (EditText) findViewById(R.id.emailField);
        nameEditText = (EditText) findViewById(R.id.nameField);
        passwordEditText = (EditText) findViewById(R.id.passwordField);
        registerButton = (Button) findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEditText.getText().toString().trim();
                final String name = nameEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();

                // hər 3 fieldin boş olub-olmamasını yoxlayırıq, boş olduğu təqdirdə alert mesajı ekrana çıxır
                if (email.isEmpty() || name.isEmpty() || password.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle(R.string.register_error_title)
                            .setMessage(R.string.register_error_message)
                            .setPositiveButton(android.R.string.ok,null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    // digər hallarda Firebase database-a user məlumatlarını yazmağa cəhd edir
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("email", email);
                                        map.put("name", name);
                                        map.put("id", mAuth.getCurrentUser().getUid());
                                        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).updateChildren(map);
                                    }else{
                                        Log.e(TAG, task.getException().toString());
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setMessage("Authentication failed.\n"+task.getException())
                                                .setTitle(R.string.register_error_title)
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                            });
                }


            }
        });



    }




}
