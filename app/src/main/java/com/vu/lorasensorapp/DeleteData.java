package com.vu.lorasensorapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeleteData {

    private static final String BASE_URL = "https://api.thingspeak.com/";
    private static final String CHANNEL_ID = "2054190";
    private static final String WRITE_API_KEY = "XUD3RJQWGZF4SWM7";

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public void deleteChannelData(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delete Data");
        builder.setMessage("Are you sure you want to delete all data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new DeleteDataTask(activity).execute();
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class DeleteDataTask extends AsyncTask<Void, Void, Boolean> {

        private final Context context;

        public DeleteDataTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String url = BASE_URL + "channels/" + CHANNEL_ID + "/feeds.json";

            HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
            httpBuilder.addQueryParameter("api_key", WRITE_API_KEY);

            Request request = new Request.Builder()
                    .url(httpBuilder.build())
                    .delete()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                String json = response.body().string();
                ResponseDto responseDto = gson.fromJson(json, ResponseDto.class);
                return responseDto.isSuccess();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Data was deleted successfully
                Toast.makeText(context, "Data deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                // There was an error deleting the data
                Toast.makeText(context, "Error deleting data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class ResponseDto {
        private boolean success;

        public boolean isSuccess() {
            return success;
        }
    }
}

