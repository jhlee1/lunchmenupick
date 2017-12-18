package ea.qa.joohan.lunchpick;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Joohan on 11/30/2017.
 */

public class TicketListAdapter extends BaseAdapter {
    private Context context;
    private List<Ticket> tickets;

    public TicketListAdapter(Context c, List<Ticket> tickets) {
        this.context = c;
        this.tickets = tickets;
    }
    @Override
    public int getCount() {
        return tickets.size();
    }

    @Override
    public Object getItem(int i) {
        return tickets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View v = View.inflate(context, R.layout.ticket, null);
        TextView ticketName = v.findViewById(R.id.ticketName);
        ticketName.setText(tickets.get(i).getName());
        ImageView deleteTicket = v.findViewById(R.id.deleteTicket);
        final int deletedIndex = i;
        deleteTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tickets.remove(deletedIndex);
                try {
                    FileOutputStream outputStream = v.getContext().openFileOutput(v.getResources().getString(R.string.ticket_filename), v.getContext().MODE_PRIVATE);
                    Gson gson = new Gson();
                    for(Ticket t :tickets) {
                        outputStream.write((gson.toJson(t)+"\n").getBytes());
                    }

                    outputStream.close();
                } catch (IOException e) {
                    Log.d("FileNotFound", "Fails to write a new routine");
                }
                notifyDataSetChanged();
            }
        });
        return v;
    }

}
