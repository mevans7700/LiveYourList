package com.evansappwriter.liveyourlist.ui;

import android.view.View;
import android.widget.TextView;

import com.evansappwriter.liveyourlist.R;
import com.evansappwriter.liveyourlist.model.User;
import com.evansappwriter.liveyourlist.util.Holder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by markevans on 7/25/16.
 */
public class UserHolder extends Holder<User> {
    //@Bind(R.id.firstname)
    TextView firstname;
    //@Bind(R.id.lastname)
    private TextView lastname;

    public UserHolder(View view){
        super(view);
        //ButterKnife.bind(this, view);
        firstname = (TextView)view.findViewById(R.id.firstname);
        lastname = (TextView)view.findViewById(R.id.lastname);
    }

    @Override
    protected void bindViews(User user) {
        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());
    }
}
