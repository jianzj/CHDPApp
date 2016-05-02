package com.chdp.chdpapp.util;

import android.widget.TextView;

import com.chdp.chdpapp.R;
import com.chdp.chdpapp.WithProcessActivity;

public class PrescriptionHelper {
    public static void setPrescriptionBasicInfo(WithProcessActivity activity) {
        TextView txtPrsName = (TextView) activity.findViewById(R.id.txt_prs_name);
        TextView txtPrsUuid = (TextView) activity.findViewById(R.id.txt_prs_uuid);
        TextView txtPrsGender = (TextView) activity.findViewById(R.id.txt_prs_gender);
        TextView txtPrsHospital = (TextView) activity.findViewById(R.id.txt_prs_hospital);
        TextView txtPrsNum = (TextView) activity.findViewById(R.id.txt_prs_num);
        txtPrsName.setText(activity.prescription.getPatient_name());
        txtPrsUuid.setText(activity.prescription.getUuid());
        txtPrsGender.setText(activity.prescription.getSex() == Constants.MAN ? "男" : "女");
        txtPrsHospital.setText(activity.prescription.getHospital_name());
        txtPrsNum.setText(activity.prescription.getPacket_num() + "帖" + (activity.prescription.getPacket_num() * 2) + "包");
    }
}
