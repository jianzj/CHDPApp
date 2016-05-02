package com.chdp.chdpapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chdp.chdpapp.bean.Herb;
import com.chdp.chdpapp.service.HerbService;
import com.chdp.chdpapp.service.ServiceGenerator;
import com.chdp.chdpapp.util.Constants;
import com.chdp.chdpapp.util.ContextHolder;
import com.chdp.chdpapp.util.PrescriptionHelper;
import com.chdp.chdpapp.util.ProcessHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckActivity extends WithProcessActivity {
    private List<Herb> decoctFirst = new ArrayList<Herb>();
    private List<Herb> decoctLater = new ArrayList<Herb>();
    private List<Herb> wrappedDecoct = new ArrayList<Herb>();

    private RadioButton radioType1;
    private RadioButton radioType2;
    private RadioButton radioType3;

    private CheckBox checkFirst;
    private TextView txtFirst;
    private CheckBox checkLater;
    private TextView txtLater;
    private CheckBox checkWrap;
    private TextView txtWrap;
    private CheckBox checkDrink;
    private TextView txtDrink;
    private CheckBox checkMelt;
    private TextView txtMelt;
    private CheckBox checkAlone;
    private TextView txtAlone;

    private Button btnCheck;
    private Button btnCheckCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        setTitle("审方处理");

        PrescriptionHelper.setPrescriptionBasicInfo(this);
        ProcessHelper.setProcessStatus(this);

        prepareHerbs();

        radioType1 = (RadioButton) findViewById(R.id.radio_type1);
        radioType2 = (RadioButton) findViewById(R.id.radio_type2);
        radioType3 = (RadioButton) findViewById(R.id.radio_type3);

        checkFirst = (CheckBox) findViewById(R.id.check_first);
        txtFirst = (TextView) findViewById(R.id.txt_first);
        checkLater = (CheckBox) findViewById(R.id.check_later);
        txtLater = (TextView) findViewById(R.id.txt_later);
        checkWrap = (CheckBox) findViewById(R.id.check_wrap);
        txtWrap = (TextView) findViewById(R.id.txt_wrap);
        checkDrink = (CheckBox) findViewById(R.id.check_drink);
        txtDrink = (TextView) findViewById(R.id.txt_drink);
        checkMelt = (CheckBox) findViewById(R.id.check_melt);
        txtMelt = (TextView) findViewById(R.id.txt_melt);
        checkAlone = (CheckBox) findViewById(R.id.check_alone);
        txtAlone = (TextView) findViewById(R.id.txt_alone);

        btnCheck = (Button) findViewById(R.id.btn_check);
        btnCheckCancel = (Button) findViewById(R.id.btn_check_cancel);

        checkFirst.setOnCheckedChangeListener(new SpecialCheckListener());
        checkLater.setOnCheckedChangeListener(new SpecialCheckListener());
        checkWrap.setOnCheckedChangeListener(new SpecialCheckListener());
        checkDrink.setOnCheckedChangeListener(new SpecialCheckListener());
        checkMelt.setOnCheckedChangeListener(new SpecialCheckListener());
        checkAlone.setOnCheckedChangeListener(new SpecialCheckListener());

        txtFirst.setOnClickListener(new SpecialClickListener());
        txtLater.setOnClickListener(new SpecialClickListener());
        txtWrap.setOnClickListener(new SpecialClickListener());
        txtDrink.setOnClickListener(new SpecialClickListener());
        txtMelt.setOnClickListener(new SpecialClickListener());
        txtAlone.setOnClickListener(new SpecialClickListener());

        btnCheck.setOnClickListener(new CheckClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data && requestCode == 200) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    break;
                default:
                    Toast.makeText(this, "条码扫描失败，请重试", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setClass(this, CheckActivity.class);
                    this.startActivity(intent);
                    this.finish();
            }
        } else {
            this.finish();
        }
    }

    private void prepareHerbs() {
        HerbService service = ServiceGenerator.create(HerbService.class, user.getSession_id());
        Call<List<Herb>> call = service.getHerbs();
        call.enqueue(new Callback<List<Herb>>() {
            @Override
            public void onResponse(Call<List<Herb>> call, Response<List<Herb>> response) {
                if (response.isSuccessful()) {
                    List<Herb> herbs = response.body();
                    for (Herb herb : herbs) {
                        if (herb.getType() == Constants.DECOCT_FIRST)
                            decoctFirst.add(herb);
                        else if (herb.getType() == Constants.DECOCT_LATER)
                            decoctLater.add(herb);
                        else if (herb.getType() == Constants.WRAPPED_DECOCT) {
                            wrappedDecoct.add(herb);
                        }
                    }
                } else {
                    Toast.makeText(ContextHolder.getContext(), "获取中药信息失败，请后退重试", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Herb>> call, Throwable t) {
                Toast.makeText(ContextHolder.getContext(), "获取中药信息失败，请后退重试", Toast.LENGTH_LONG).show();
            }
        });
    }

    private class SpecialCheckListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (buttonView.getId() == checkFirst.getId()) {
                    ListView listHerbs = new ListView(CheckActivity.this);
                    listHerbs.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listHerbs.setAdapter(new ArrayAdapter<Herb>(CheckActivity.this, android.R.layout.simple_list_item_multiple_choice, decoctFirst));
                    createHerbDialog(listHerbs, decoctFirst, txtFirst, checkFirst);
                } else if (buttonView.getId() == checkLater.getId()) {
                    ListView listHerbs = new ListView(CheckActivity.this);
                    listHerbs.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listHerbs.setAdapter(new ArrayAdapter<Herb>(CheckActivity.this, android.R.layout.simple_list_item_multiple_choice, decoctLater));
                    createHerbDialog(listHerbs, decoctLater, txtLater, checkLater);
                } else if (buttonView.getId() == checkWrap.getId()) {
                    ListView listHerbs = new ListView(CheckActivity.this);
                    listHerbs.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listHerbs.setAdapter(new ArrayAdapter<Herb>(CheckActivity.this, android.R.layout.simple_list_item_multiple_choice, wrappedDecoct));
                    createHerbDialog(listHerbs, wrappedDecoct, txtWrap, checkWrap);
                } else if (buttonView.getId() == checkDrink.getId()) {
                    createTextDialog(txtDrink, checkDrink);
                } else if (buttonView.getId() == checkMelt.getId()) {
                    createTextDialog(txtMelt, checkMelt);
                } else if (buttonView.getId() == checkAlone.getId()) {
                    createTextDialog(txtAlone, checkAlone);
                }
            } else {
                if (buttonView.getId() == checkFirst.getId()) {
                    txtFirst.setText("");
                } else if (buttonView.getId() == checkLater.getId()) {
                    txtLater.setText("");
                } else if (buttonView.getId() == checkWrap.getId()) {
                    txtWrap.setText("");
                } else if (buttonView.getId() == checkDrink.getId()) {
                    txtDrink.setText("");
                } else if (buttonView.getId() == checkMelt.getId()) {
                    txtMelt.setText("");
                } else if (buttonView.getId() == checkAlone.getId()) {
                    txtAlone.setText("");
                }
            }
        }
    }

    private class SpecialClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == txtFirst.getId()) {
                ListView listHerbs = new ListView(CheckActivity.this);
                listHerbs.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listHerbs.setAdapter(new ArrayAdapter<Herb>(CheckActivity.this, android.R.layout.simple_list_item_multiple_choice, decoctFirst));
                createHerbDialog(listHerbs, decoctFirst, txtFirst, checkFirst);
            } else if (v.getId() == txtLater.getId()) {
                ListView listHerbs = new ListView(CheckActivity.this);
                listHerbs.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listHerbs.setAdapter(new ArrayAdapter<Herb>(CheckActivity.this, android.R.layout.simple_list_item_multiple_choice, decoctLater));
                createHerbDialog(listHerbs, decoctLater, txtLater, checkLater);
            } else if (v.getId() == txtWrap.getId()) {
                ListView listHerbs = new ListView(CheckActivity.this);
                listHerbs.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listHerbs.setAdapter(new ArrayAdapter<Herb>(CheckActivity.this, android.R.layout.simple_list_item_multiple_choice, wrappedDecoct));
                createHerbDialog(listHerbs, wrappedDecoct, txtWrap, checkWrap);
            } else if (v.getId() == txtDrink.getId()) {
                createTextDialog(txtDrink, checkDrink);
            } else if (v.getId() == txtMelt.getId()) {
                createTextDialog(txtMelt, checkMelt);
            } else if (v.getId() == txtAlone.getId()) {
                createTextDialog(txtAlone, checkAlone);
            }
        }
    }

    private void createHerbDialog(final ListView listview, final List<Herb> list, final TextView txtHerbs, final CheckBox checkbox) {
        String[] items = txtHerbs.getText().toString().split(" ");
        int i = 0, j = 0;
        while (i < items.length) {
            if (items[i].equals("")) {
                i++;
            } else if (list.get(j).getName().equals(items[i])) {
                listview.setItemChecked(j, true);
                i++;
                j++;
            } else
                j++;
        }

        new AlertDialog.Builder(CheckActivity.this).setTitle("选择特殊处理项").setView(listview)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectItem = "";
                        SparseBooleanArray lastCheckedId = listview.getCheckedItemPositions();
                        SparseBooleanArray checkedId = listview.getCheckedItemPositions();
                        for (int i = 0; i < checkedId.size(); i++) {
                            if (checkedId.valueAt(i)) {
                                selectItem += list.get(checkedId.keyAt(i)) + " ";
                            }
                        }
                        if (selectItem.equals(""))
                            checkbox.setChecked(false);
                        else
                            txtHerbs.setText(selectItem);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (txtHerbs.getText().toString().equals(""))
                            checkbox.setChecked(false);
                    }
                }).show();
    }

    private void createTextDialog(final TextView textview, final CheckBox checkbox) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckActivity.this).setTitle("输入特殊处理项");
        final EditText input = new EditText(this);
        input.setText(textview.getText().toString());
        builder.setView(input);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().equals(""))
                    checkbox.setChecked(false);
                else
                    textview.setText(input.getText());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (textview.getText().toString().equals(""))
                    checkbox.setChecked(false);
            }
        }).show();
    }

    private class CheckClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
}
