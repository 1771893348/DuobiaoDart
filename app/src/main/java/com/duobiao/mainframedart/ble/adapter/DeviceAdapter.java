package com.duobiao.mainframedart.ble.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clj.fastble.data.BleDevice;
import com.duobiao.mainframedart.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @allan modify 2019/02/27
 */
public class DeviceAdapter extends BaseAdapter {

    private Context context;
    private List<BleDevice> bleDeviceList;

    public DeviceAdapter(Context context) {
        this.context = context;
        bleDeviceList = new ArrayList<>();
    }


    //    public void updateList(List<BleDevice> bleDeviceList) {
    //        this.bleDeviceList.clear();
    //        this.bleDeviceList.addAll(bleDeviceList);
    //        notifyDataSetChanged();
    //    }

    public void addData(BleDevice device) {
        bleDeviceList.add(device);
        notifyDataSetChanged();
    }

    public void clear() {
        bleDeviceList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return bleDeviceList.size();
    }

    @Override
    public BleDevice getItem(int position) {
        if (position > bleDeviceList.size()) return null;
        return bleDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.item_ble_select, null);
            holder = new ViewHolder();
            holder.txt_name = (TextView) convertView.findViewById(R.id.ble_scan_select_name);
            holder.txt_mac = (TextView) convertView.findViewById(R.id.ble_scan_select_ass);
            convertView.setTag(holder);
        }

        BleDevice bleDevice = getItem(position);
        holder.txt_name.setText(getBleName(bleDevice.getName()));
        holder.txt_mac.setText(bleDevice.getMac());

        return convertView;
    }


    public String getBleName(String bleName) {
        if (bleName == null) {
            return "";
        }
        String nameTmp;
        switch (bleName.toLowerCase()) {
            case "duobiaodarts":
                nameTmp = "DB-2";
                break;
            case "duobiao_h_01_01":
                nameTmp = "DB-6";
                break;
            case "eagledart":
                nameTmp = "DB-1";
                break;
            case "duobiao_h01_01":
                nameTmp = "DB-6";
                break;
            default:
                nameTmp = "";
                break;
        }
        return nameTmp;
    }

    class ViewHolder {
        TextView txt_name;
        TextView txt_mac;
    }

}
