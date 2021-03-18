package com.example.personaldictonary;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements Filterable {
    Context context;
    private List<Note> allNotes;
    List<Note> copyAllNotes;
    private DataBaseHelper databaseHelper;
    TextToSpeech t1;

    public CustomAdapter(Context context, List<Note> allNotes) {
        this.context = context;
        this.allNotes = allNotes;
        this.context=context;
        databaseHelper=new DataBaseHelper(context);

        copyAllNotes = new ArrayList<>(allNotes);//for searchView//dataList's copy

        t1=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(context);
       View view= layoutInflater.inflate(R.layout.sample,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.english.setText(allNotes.get(position).getEnglish());
        holder.bangla.setText(allNotes.get(position).getBangla());

        holder.voiceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String toSpeak=holder.english.getText().toString();
                t1.setSpeechRate(.5f);
                t1.setPitch(1.0f);
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

            }
        });




        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder  = new androidx.appcompat.app.AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_operation,null);

                builder.setView(view);
                final androidx.appcompat.app.AlertDialog alertDialog = builder.create();

                TextView updateTextView=view.findViewById(R.id.updateTextViewId);
                TextView deleteTextView=view.findViewById(R.id.deleteTextViewId);
                TextView cancelTextView=view.findViewById(R.id.cancelTextViewId);

                updateTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog(position);
                        alertDialog.dismiss();

                    }
                });

                deleteTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int status = databaseHelper.deleteData(allNotes.get(position).getId());
                        if (status == 1){
                            allNotes.remove(allNotes.get(position));
                            alertDialog.dismiss();
                            notifyDataSetChanged();
                        }else {
                        }
                    }
                });

                cancelTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });

                alertDialog.show();
                return true;
            }
        });

    }
    @Override
    public int getItemCount() {
        return allNotes.size();
    }

    @Override
    public Filter getFilter() {
        return filterData;
    }


    Filter filterData =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Note> filterList=new ArrayList<>();
            //for filter data keeping
            if (charSequence==null||charSequence.length()==0){
                filterList.addAll(copyAllNotes);
            }
            else{
                String value=charSequence.toString().toLowerCase().trim();
                for (Note notes:copyAllNotes){
                    if (notes.getEnglish().toLowerCase().trim().contains(value)||notes.getBangla().toLowerCase().trim().contains(value)){
                        filterList.add(notes);
                    }

                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            allNotes.clear();
            allNotes.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView english,bangla;
        ImageView voiceImage;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            english= itemView.findViewById(R.id.englishTextViewId);
            bangla= itemView.findViewById(R.id.banglaTextViewId);
            voiceImage= itemView.findViewById(R.id.voiceImageViewId);

        }
    }


    private void customDialog(final int position) {

        androidx.appcompat.app.AlertDialog.Builder builder  = new androidx.appcompat.app.AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.input_box,null);
        builder.setView(view);
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();

        final EditText englishEditText       = view.findViewById(R.id.englishEditTextId);
        final EditText banglaEditText        = view.findViewById(R.id.banglaEditTextId);

        englishEditText.setText(allNotes.get(position).getEnglish());
        banglaEditText.setText(allNotes.get(position).getBangla());
        Button saveButton    = view.findViewById(R.id.saveButtonId);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String englishValue       = englishEditText.getText().toString();
                String banglaValue = banglaEditText.getText().toString();

                if (englishValue.isEmpty()){
                    englishEditText.setError("Enter name");
                    return;
                }else if (banglaValue.isEmpty()){
                    banglaEditText.setError("Enter location");
                    return;
                }

                long status = databaseHelper.updateData(new Note(allNotes.get(position).getId(),
                        englishValue,banglaValue));
                if (status == 1){
                    alertDialog.dismiss();
                    allNotes.clear();
                    allNotes.addAll((Collection<? extends Note>) databaseHelper.getAllNotes());
                    notifyDataSetChanged();
                    Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show();
                }else {
                    alertDialog.dismiss();
                    Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();

    }

}
