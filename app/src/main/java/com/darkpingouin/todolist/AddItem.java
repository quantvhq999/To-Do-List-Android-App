package com.darkpingouin.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.darkpingouin.todolist.R.id.date;


public class AddItem extends AppCompatActivity {
    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private Switch mRepeatSwitch;
    private String mActive;

    private Calendar calendar;
    public Spinner spinner2;
    private TextView dateView, timeView;
    private int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mRepeatText = (TextView) findViewById(R.id.set_repeat);
        mRepeatNoText = (TextView) findViewById(R.id.set_repeat_no);
        mRepeatTypeText = (TextView) findViewById(R.id.set_repeat_type);
        mRepeatSwitch = (Switch) findViewById(R.id.repeat_switch);
        // Initialize default values
        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Giờ";

        dateView = (TextView) findViewById(date);
        timeView = (TextView) findViewById(R.id.time);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        try {
            showDate(year, month + 1, day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        showTime(hour, minute);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        addItemsOnSpinner2();
    }


    public void addItemsOnSpinner2() {
        List<String> list = new ArrayList<>();
        int i = 0;
        while (i < MainActivity.getCat().size()) {
            list.add(MainActivity.getCat().get(i).getName());
            i++;
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                findViewById(R.id.textBar).setBackgroundColor(MainActivity.getCat().get(position).getColor());
                findViewById(R.id.title).setBackgroundColor(MainActivity.getCat().get(position).getColor());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        spinner2.setAdapter(dataAdapter);
    }

    @SuppressWarnings("deprecation")

    public void setDate(View view) {
        showDialog(999);
    }


    public void setTime(View view) {
        showDialog(998);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }

        if (id == 998) {
            return new TimePickerDialog(this, myTimeListener, hour, minute, true);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    try {
                        showDate(arg1, arg2 + 1, arg3);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };

    private TimePickerDialog.OnTimeSetListener myTimeListener = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker arg0,
                                      int arg1, int arg2) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showTime(arg1, arg2);
                }
            };

    /**
     * Affiche la date choisie
     * @param year année
     * @param month mois
     * @param day jour
     * @throws ParseException
     */
    private void showDate(int year, int month, int day) throws ParseException {
        String d = (String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year);
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date MyDate = newDateFormat.parse(d);
        newDateFormat.applyPattern("EE d MMM yyyy");
        String MySDate = newDateFormat.format(MyDate);
        dateView.setText(MySDate);

    }

    /**
     * Affiche le temps dans la text view time
     * @param hour heure
     * @param minute minute
     */
    private void showTime(int hour, int minute) {
        timeView.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
    }

    /**
     * Ferme la vue
     * @param view view
     */
    public void cancel(View view)
    {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
    }


    public void save(View view) throws ParseException {
        Date current = new Date();
        String title = ((TextView) findViewById(R.id.title)).getText().toString();
        String txt = ((TextView) findViewById(R.id.txt)).getText().toString().replace('<', ' ');
        if (title.equals("") || txt.equals(""))
            Toast.makeText(getApplicationContext(), "Tiêu đề và mô tả không thể trống !", Toast.LENGTH_SHORT).show();
        else {
            String d = ((TextView) findViewById(R.id.date)).getText().toString() + " " + ((TextView) findViewById(R.id.time)).getText().toString();
            SimpleDateFormat newDateFormat = new SimpleDateFormat("EE d MMM yyyy k:m");
            Date date = newDateFormat.parse(d);
            String categorie = String.valueOf(spinner2.getSelectedItem());
            if (date.after(current)) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("title", title);
                returnIntent.putExtra("txt", txt);
                returnIntent.putExtra("date", d);
                returnIntent.putExtra("categorie", categorie);
                returnIntent.putExtra("edit", "false");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Bạn không thể chọn ngày đã qua !", Toast.LENGTH_SHORT).show();
        }
    }

    //Onclick Repeat
    public void setRepeatNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Nhập số lần");

        // Create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mRepeatNo = Integer.toString(1);
                            mRepeatNoText.setText(mRepeatNo);
                            mRepeatText.setText(mRepeatNo + " " + mRepeatType + " một lần");
                        }
                        else {
                            mRepeatNo = input.getText().toString().trim();
                            mRepeatNoText.setText(mRepeatNo);
                            mRepeatText.setText(mRepeatNo + " " + mRepeatType + " một lần");
                        }
                    }
                });
        alert.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    // On clicking the repeat switch
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepeat = "true";
            mRepeatText.setText(mRepeatNo + " " + mRepeatType + " một lần");
        } else {
            mRepeat = "false";
            mRepeatText.setText(R.string.repeat_off);
        }
    }

    // On clicking repeat type button
    public void selectRepeatType(View v){
        final String[] items = new String[5];

        items[0] = "Phút";
        items[1] = "Giờ";
        items[2] = "Ngày";
        items[3] = "Tuần";
        items[4] = "Tháng";
        // Create List Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn loại thời gian");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mRepeatType = items[item];
                mRepeatTypeText.setText(mRepeatType);
                mRepeatText.setText(mRepeatNo + " " + mRepeatType + " một lần");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
