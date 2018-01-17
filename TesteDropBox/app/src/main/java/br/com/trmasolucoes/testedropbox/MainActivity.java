package br.com.trmasolucoes.testedropbox;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final String APP_KEY = "xhdd4zo0v5gxvla";
    private static final String APP_SECRET = "a71onnb19uu0kf3";

    private static final String ACCOUNT_PREFS_NAME = "prefs_drobbox";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    private ListView listView;
    private Button btnLogin;
    private Button btnLogout;
    private Button btnLoad;
    private List<DropboxAPI.Entry> list;

    //widgets to create, update e delete file
    private Button btnUploadFile;
    private Button btnUpdateFile;
    private Button btnDownloadFile;
    private ImageView imageView;

    private DropboxAPI<AndroidAuthSession> dropboxAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializa a lista do dropbox
        list = new ArrayList<DropboxAPI.Entry>();

        //carrega a sessão do dropbox
        AndroidAuthSession session = buildSession();

        dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        listView = (ListView) findViewById(R.id.listView);

        //widgets to create, update e delete file
        btnUploadFile = (Button) findViewById(R.id.btnUploadFile);
        btnUpdateFile = (Button) findViewById(R.id.btnUpdateFile);
        btnDownloadFile = (Button) findViewById(R.id.btnDownloadFile);
        imageView = (ImageView) findViewById(R.id.imageView);


        enableViews(dropboxAPI.getSession().isLinked());
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair keyPair = new AppKeyPair(APP_KEY, APP_SECRET);

        AndroidAuthSession session = new AndroidAuthSession(keyPair);
        loadSession(session);
        return session;
    }

    private void loadSession(AndroidAuthSession session) {
        SharedPreferences preferences = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String token = preferences.getString(ACCESS_TOKEN, null);

        if (token == null || token.length() == 0) {
            return;
        } else {
            session.setOAuth2AccessToken(token);
        }
    }

    public void enableViews(boolean status) {
        if (status) {
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
            btnLoad.setVisibility(View.VISIBLE);
            btnUpdateFile.setVisibility(View.VISIBLE);
            btnUploadFile.setVisibility(View.VISIBLE);
            btnDownloadFile.setVisibility(View.VISIBLE);
        } else {
            btnLoad.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
            btnUpdateFile.setVisibility(View.GONE);
            btnUploadFile.setVisibility(View.GONE);
            btnDownloadFile.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        AndroidAuthSession session = dropboxAPI.getSession();

        if (session.authenticationSuccessful()) {
            session.finishAuthentication();

            saveLogged(session);
            enableViews(true);
        }
    }

    private void saveLogged(AndroidAuthSession session) {
        String token = session.getOAuth2AccessToken();
        if (token != null) {
            SharedPreferences preferences = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(ACCESS_TOKEN, token);
            editor.commit();
        }
    }

    public void login(View view) {
        dropboxAPI.getSession().startOAuth2Authentication(MainActivity.this);
    }

    public void logout(View view) {
        dropboxAPI.getSession().unlink();

        SharedPreferences preferences = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

        enableViews(false);
    }

    public void getDocs(DropboxAPI.Entry entry) throws DropboxException {
        list.add(entry);
        List<DropboxAPI.Entry> elist = entry.contents;
        if (elist != null) {
            for (DropboxAPI.Entry e : elist) {
                e = dropboxAPI.metadata(e.path, 0, null, true, null);
                getDocs(e);
            }
        }
    }

    public void load(View view) {
        new Thread() {
            @Override
            public void run() {

                try {
                    DropboxAPI.Entry entry = dropboxAPI.metadata("/WorkSpace", 0, null, true, null);
                    getDocs(entry);
                } catch (DropboxException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(new AdapterDropbox(MainActivity.this, list));
                    }
                });
            }
        }.start();
    }


    //Methods to create, update e delete files in dropbox

    public void downloadFile(View view) {
        btnDownloadFile.setText("Baixando...");
        btnDownloadFile.setEnabled(false);

        new Thread() {
            @Override
            public void run() {
                File file = null;
                try {

                    file = new File(Environment.getExternalStorageDirectory(),"Pictures/imagemBaixada.jpg");
                    FileOutputStream outputStream = new FileOutputStream(file);

                    DropBoxProgressListener progressListener = new DropBoxProgressListener(btnDownloadFile);
                    DropboxAPI.DropboxFileInfo info = dropboxAPI.getFile("/ExemploDB/image.jpg", null, outputStream, progressListener);

                    Log.i("Script","Revison Hash (downloadFile):  " + info.getMetadata().rev);

                } catch (DropboxException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                final String path = file == null ? null: file.getAbsolutePath();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnDownloadFile.setText("Download file");
                        btnDownloadFile.setEnabled(true);

                        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                    }
                });
            }
        }.start();
    }

    public void uploadFile(View view) {
        btnUploadFile.setText("Enviando...");
        btnUploadFile.setEnabled(false);

        new Thread() {
            @Override
            public void run() {
                try {

                    try {
                        //Tenta pegar o diretorio, se ele não existir, gera a exceção, e é criado
                        dropboxAPI.metadata("/ExemploDB", 0, null, false, null);
                    } catch (DropboxServerException e1) {
                        dropboxAPI.createFolder("/ExemploDB");
                    }

                    File file = new File(Environment.getExternalStorageDirectory(),"Pictures/image.jpg");
                    FileInputStream inputStream = new FileInputStream(file);

                    DropBoxProgressListener progressListener = new DropBoxProgressListener(btnUpdateFile);
                    DropboxAPI.Entry response = dropboxAPI.putFile("/ExemploDB/image.jpg", inputStream, file.length(), null, progressListener);

                    Log.i("Script","Revison Hash (uploadFile): " + response.rev);

                } catch (DropboxException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnUploadFile.setText("Upload file");
                        btnUploadFile.setEnabled(true);
                    }
                });
            }
        }.start();
    }

    public void updateFile(View view) {
        btnUpdateFile.setText("Atualizando...");
        btnUpdateFile.setEnabled(false);

        new Thread() {
            @Override
            public void run() {
                try {

                    File file = new File(Environment.getExternalStorageDirectory(),"Pictures/image2.jpg");
                    FileInputStream inputStream = new FileInputStream(file);

                    DropBoxProgressListener progressListener = new DropBoxProgressListener(btnUpdateFile);
                    DropboxAPI.Entry response = dropboxAPI.putFileOverwrite("/ExemploDB/image1.jpg", inputStream, file.length(), progressListener);

                    Log.i("Script","Revison Hash (updateFile): " + response.rev);

                } catch (DropboxException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnUpdateFile.setText("Update file");
                        btnUpdateFile.setEnabled(true);
                    }
                });
            }
        }.start();
    }

    public void deleteFile(View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    dropboxAPI.delete("/ExemploDB/image.jpg");
                } catch (DropboxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void deleteFolder(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    dropboxAPI.delete("/ExemploDB");
                } catch (DropboxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class DropBoxProgressListener extends ProgressListener{
        private Button btn;

        public DropBoxProgressListener(Button btn) {
            super();
            this.btn = btn;
        }

        @Override
        public void onProgress(long now, long total) {
            final double val = (long)((double)now / (double)total)*100;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn.setText(btn.getText() + " " + val + "%");
                }
            });
        }

        @Override
        public long progressInterval() {
            return (100);
        }
    }
}
