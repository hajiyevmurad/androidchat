package murad.androidchat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import murad.androidchat.Model.Message;

/**
 * Created by Administrator on 17.07.2016.
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Message> messageArrayList;
    private String userId;

    private static String today;
    private int SELF = 100;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView message, timestamp;
        public ViewHolder(View v){
            super(v);
            message = (TextView) v.findViewById(R.id.message);
            timestamp = (TextView) v.findViewById(R.id.timestamp);
        }
    }

    public ChatRoomAdapter(Context mContext, ArrayList<Message> messageArrayList, String userId) {
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;
        this.userId = userId;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == SELF){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_self, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_other,parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if (message.getUser().getId().equals(userId))
            return SELF;
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        final ViewHolder myViewHolder = (ViewHolder) holder;
        myViewHolder.message.setText(message.getMessage());

        String timestamp = getTimeStamp(message.getCreatedAt());

        if (message.getUser().getName() != null)
            timestamp = message.getUser().getName() + ", " +timestamp;

        myViewHolder.timestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:s");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
}
