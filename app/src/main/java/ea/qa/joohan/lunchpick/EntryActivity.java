package ea.qa.joohan.lunchpick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class EntryActivity extends Activity {

    private EditText ticketInputName;
    private Button addButton;
    private ArrayList<Ticket> tickets;
    private Gson gson;
    private String ticketFileName;
    private ListView ticketListView;
    private TextView nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        addButton = findViewById(R.id.addTicket);
        ticketInputName = findViewById(R.id.inputTicketName);
        tickets = new ArrayList<>();
        gson = new Gson();
        nextButton = findViewById(R.id.nextButton);
        ticketFileName = getResources().getString(R.string.ticket_filename);
        try {
            FileInputStream inputStream = this.openFileInput(ticketFileName);
            Scanner s = new Scanner(inputStream);
            while(s.hasNextLine()) {
                tickets.add(gson.fromJson(s.nextLine(), Ticket.class));
            }
        } catch (IOException e ) {
            try {
                FileOutputStream outputStream = openFileOutput(ticketFileName, MODE_PRIVATE);
                outputStream.close();
            } catch (IOException e2) {
                Log.d("outputstream exception", "Fail to create a routine file");

            }
        }
//        for(Ticket t : tickets) {
//            Log.d("TicketList", t.getName() + " ");
//        }

        ticketListView = findViewById(R.id.ticketList);
        final TicketListAdapter ticketListAdapter = new TicketListAdapter(this,tickets);
        ticketListView.setAdapter(ticketListAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTicket();
                ticketListAdapter.notifyDataSetChanged();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ticketIntent = new Intent(getContext(), MainActivity.class);
                ticketIntent.putParcelableArrayListExtra("tickets", tickets);
                getContext().startActivity(ticketIntent);
            }
        });
        ticketInputName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    addTicket();
                    ticketListAdapter.notifyDataSetChanged();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
    private Context getContext () {
        return this;
    }
    private void addTicket () {
        Ticket tmpTicket = new Ticket(ticketInputName.getText().toString());
        tickets.add(tmpTicket);
        try {
            FileOutputStream outputStream = openFileOutput(ticketFileName, MODE_PRIVATE);
            for(Ticket t : tickets) {
                outputStream.write((gson.toJson(t)+"\n").getBytes());
            }
            Log.d("Saving", gson.toJson(tmpTicket) + "Is being saved");
            ticketInputName.setText(null);
            ticketListView.invalidate();
            outputStream.close();
        } catch (IOException e1) {
            Log.d("FileNotFound", "Fails to write a new routine");
        }
    }
}
