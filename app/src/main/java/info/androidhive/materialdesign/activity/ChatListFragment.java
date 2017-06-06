package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.database.Database;
import info.androidhive.materialdesign.model.Chat;
import info.androidhive.materialdesign.model.Constatnts;

public class ChatListFragment extends Fragment {

    //Initialization of Variables
    ArrayList<Chat> chatArr;
    // ArrayList<Customers> custArr;
    ListView chat_ListView;
    Database db = null;
    CodeLearnAdapter chapterListAdapter;
    RelativeLayout rl_ContactsInvisible;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_list, container, false);

        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        hideSoftKeyboard();
        chat_ListView = (ListView) rootView.findViewById(R.id.chat_listView);
        rl_ContactsInvisible = (RelativeLayout) rootView.findViewById(R.id.relativeLayout1);

      /*  *//** Customer Support Chat **//*
        CardView Customer_supportChat = (CardView) rootView.findViewById(R.id.Card_Customer_SupportChat);

        Customer_supportChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constatnts.VENDOR_ID = "1786";
                Constatnts.VENDOR_NAME = "Customer Chat Support";
                Constatnts.VENDOR_ADDRESS = "Hyderabad";

                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
*/


        //Array To Store ChatS
        chatArr = new ArrayList<Chat>();

        //Db function to get all previous chats
        db = new Database(getActivity());
        chatArr = (ArrayList<Chat>) db.getAllChatCustomer();

        if(chatArr.size() != 0)
        {
            chapterListAdapter = new CodeLearnAdapter();
            chat_ListView.setAdapter(chapterListAdapter);
            rl_ContactsInvisible.setVisibility(View.INVISIBLE);
        }
        else {
            rl_ContactsInvisible.setVisibility(View.VISIBLE);
            chat_ListView.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    //hide keyboard
    public void hideSoftKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


  //Class File for displaying chat contacts
    public class  CodeLearnAdapter extends BaseAdapter {

        List<Chat> codeLearnChapterList = chatArr;

        @Override
        public int getCount() {
            return codeLearnChapterList.size();
        }

        @Override
        public Chat getItem(int arg0) {
            return codeLearnChapterList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        //displaying in list cust_name,phn_no
        @Override
        public View getView(final int arg0, View arg1, ViewGroup arg2)
        {
            if(arg1==null)
            {
                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.chatcontacts, arg2,false);
            }

            //ImageView chptrChatImage = (ImageView)arg1.findViewById(R.id.chatContactImageView);
            TextView chptrName = (TextView)arg1.findViewById(R.id.nameTextView);
            TextView chptrAddress = (TextView)arg1.findViewById(R.id.TextvAddress);
            TextView chaptrVendorType = (TextView)arg1.findViewById(R.id.txtV_vendorType);
            RelativeLayout rel_layout_chat_contacts = (RelativeLayout) arg1.findViewById(R.id.rel_layout_chat_contacts);
            final Chat chptr = codeLearnChapterList.get(arg0);

            arg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(getActivity(), Chat_Activity.class);
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    //Constatnts cons = new Constatnts();
                    Constatnts.VENDOR_ID = "" + chptr.getVendorID();
                    Constatnts.VENDOR_NAME = chptr.getVendorName();
                    Constatnts.VENDOR_ADDRESS = chptr.getVendor_AREA();
                    Constatnts.SL_CAT_ID = chptr.getVendorType() + "";
                    startActivity(intent);

                }
            });

            chptrName.setText(chptr.getVendorName());
            chptrAddress.setText(chptr.getVendor_AREA());
            chaptrVendorType.setText(""+ chptr.getVendorType());

            return arg1;
        }
    }

    @Override
    public void onResume()
    {
        chatArr = new ArrayList<Chat>();
        db = new Database(getActivity());
        chatArr = (ArrayList<Chat>) db.getAllChatCustomer();
        if(chatArr.size() != 0){
            chapterListAdapter = new CodeLearnAdapter();
            chat_ListView.setAdapter(chapterListAdapter);
            chapterListAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}
