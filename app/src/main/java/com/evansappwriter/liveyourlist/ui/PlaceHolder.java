package com.evansappwriter.liveyourlist.ui;

import android.view.View;
import android.widget.TextView;

import com.evansappwriter.liveyourlist.R;
import com.evansappwriter.liveyourlist.model.Business;
import com.evansappwriter.liveyourlist.model.Place;
import com.evansappwriter.liveyourlist.util.Holder;

/**
 * Created by markevans on 8/19/16.
 */
public class PlaceHolder extends Holder<Place> {
    //@Bind(R.id.firstname)
    TextView firstname;
    //@Bind(R.id.lastname)
    private TextView lastname;

    public PlaceHolder(View view){
        super(view);
        //ButterKnife.bind(this, view);
        firstname = (TextView)view.findViewById(R.id.firstname);
        lastname = (TextView)view.findViewById(R.id.lastname);
    }

    @Override
    protected void bindViews(Place data) {
        firstname.setText(data.getFirstName());
        lastname.setText(data.getLastName());
    }
}
