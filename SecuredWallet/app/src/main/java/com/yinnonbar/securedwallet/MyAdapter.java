package com.yinnonbar.securedwallet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Yinnon Bratspiess on 07/04/2016.
 */
public class MyAdapter extends ArrayAdapter<WalletItem> {


        private static final String TAG = "tag";
        private Activity activity;
        private static LayoutInflater inflater = null;
        private ArrayList<WalletItem> items = null;

        public static class ViewHolder {
            public TextView key;
            public TextView value;
        }
        public MyAdapter(Context context, int resource, ArrayList<WalletItem> itemsArr) {
            super(context, resource, itemsArr);
            try {
                this.activity = (Activity) context;
                this.items = itemsArr;

                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            } catch (Exception e) {
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            try {
                if (convertView == null) {
                    view = inflater.inflate(R.layout.row, null);
                    holder = new ViewHolder();
                    holder.key = (TextView) view.findViewById(R.id.itemKey);
                    holder.value = (TextView) view.findViewById(R.id.itemValue);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }
                holder.key.setText(items.get(position).getKey());
                holder.value.setText(items.get(position).getValue());

            }
            catch (Exception e) {
            }
            return view;
        }

}
