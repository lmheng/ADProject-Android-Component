package sg.edu.iss.mindmatters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GetHelpAdapter extends ArrayAdapter {

    private final Context context;

    protected int[] fnames, description;
    protected String[] phone, externalURL;

    public static final String EXTERNAL_URL = "externalUrl";

    public GetHelpAdapter(Context context, int resId){
        super(context, resId);
        this.context = context;
    }

    public void setData(int[] fnames, int[] description, String[] phone, String[] externalURL){
        this.fnames = fnames;
        this.description = description;
        this.phone = phone;
        this.externalURL = externalURL;

        for(int i = 0; i < fnames.length; i++){
            add(null);
        }
    }

    public View getView(int pos, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.get_help_list_row, parent, false);
        }

            TextView name = view.findViewById(R.id.gethelp_name);
            name.setText(fnames[pos]);

            TextView desc = view.findViewById(R.id.gethelp_description);
            desc.setText(description[pos]);

        Button call = view.findViewById(R.id.call);
        call.setOnClickListener(v -> {
            Intent intent2 = new Intent(Intent.ACTION_DIAL);
            intent2.setData(Uri.parse(phone[pos]));
            context.startActivity(intent2);
        });

        Button visit = view.findViewById(R.id.visit);
        visit.setOnClickListener(v -> launchExternalPage(externalURL[pos]));

        return view;
    }

    void launchExternalPage(String externalUrl) {
        Intent intent = new Intent(context, GetHelpView.class);
        intent.putExtra(EXTERNAL_URL, externalUrl);
        context.startActivity(intent);

    }

}