package com.mygdx.game;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.games.snapshot.Snapshots;
import com.google.example.games.basegameutils.GameHelper;
import com.mygdx.game.libgdx.MyCallback;
import com.mygdx.game.libgdx.PlayServices;
import com.mygdx.game.libgdx.Screens.MyGdxGame;

import java.io.IOException;
import java.util.Calendar;

import invaders.earth.R;

public class AndroidLauncher extends AndroidApplication implements PlayServices{

    MyGdxGame game;

    int test = 0;
    public boolean finishedLoad = false;

	private GameHelper gameHelper;
	private final static int requestCode = 1;
	private static final int RC_SAVED_GAMES = 9009;
	private String NAME_GAME_SAVED = "mySave6";
	Snapshot snapshot2 = null;
	byte[] mSaveGameData = null;
    ProgressDialog mProgressDialog;
    private int MAX_SNAPSHOT_RESOLVE_RETRIES = 2;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

/*
        layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
*/

		initializeGameHelper();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
    /*    View gameView = initializeForView(new MyGdxGame(this), config);
        layout.addView(gameView);
        setContentView(layout);

        */
        game = new MyGdxGame(this);
		initialize(game, config);
	}

	public void initializeGameHelper() {
		gameHelper = new GameHelper(this, GameHelper.CLIENT_ALL);
		gameHelper.enableDebugLog(false);
		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
			@Override
			public void onSignInFailed() {

			}

			@Override
			public void onSignInSucceeded() {

			}
		};
		gameHelper.setup(gameHelperListener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		gameHelper.onActivityResult(requestCode, resultCode, intent);
		//TODO SE NÃO DER CERTO O onActivityResult no GameHelper, implementar aqui
	}

	@Override
	public void signIn()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					gameHelper.beginUserInitiatedSignIn();
					Log.d("conectando", "SignIn");
				}
			});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					gameHelper.signOut();
				}
			});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void rateGame()
	{
		String str = "Your PlayStore Link";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void unlockAchievement(String achievimentId)
	{
		Games.Achievements.unlock(gameHelper.getApiClient(),
				achievimentId);
	}

	@Override
	public boolean submitScore(int highScore)
	{
		if (isSignedIn() == true)
		{
			Games.Leaderboards.submitScore(gameHelper.getApiClient(),
					getString(R.string.leaderboard_highest), highScore);
			return true;
		}
		else	return false;
	}

	@Override
	public void showAchievement()
	{
		if (isSignedIn() == true)
		{
			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
		}
		else
		{
			signIn();
		}
	}

	@Override
	public void showScore()
	{
		if (isSignedIn() == true)
		{
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
					getString(R.string.leaderboard_highest)), requestCode);
		}
		else
		{
			signIn();
		}
	}

	@Override
	public boolean isSignedIn()
	{
		return gameHelper.isSignedIn();
	}

	@Override
	public void toast(final String text) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
			}
		});

	}

	@Override
	public void saveGame(String snapName, final byte[] data, String desc) {
        if(isSignedIn()){
            Log.d("saveGame", "saveGame");
            AsyncTask<Void, Void, Snapshots.OpenSnapshotResult> task =
                    new AsyncTask<Void, Void, Snapshots.OpenSnapshotResult>() {
                        @Override
                        protected Snapshots.OpenSnapshotResult doInBackground(Void... params) {
                            Snapshots.OpenSnapshotResult result = Games.Snapshots.open(gameHelper.getApiClient(),
                                    NAME_GAME_SAVED, true).await();
                            return result;
                        }

                        @Override
                        protected void onPostExecute(Snapshots.OpenSnapshotResult result) {
                            Snapshot toWrite = processSnapshotOpenResult(result, 0);
                            Log.i("saveGame", writeSnapshot(toWrite, data));
							Log.d("saveGame", "JOGO SALVO NA CLOUD");
                        }
                    };

            task.execute();

        }
        //signIn();

        /*
        if (isSignedIn()){
            Games.Snapshots.open(gameHelper.getApiClient(), NAME_GAME_SAVED, true);
            Log.d("saveGame", "Snapshot opened");

            SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                    .setDescription(desc)
                    //.setCoverImage(coverImg)
            .build();
            if (snapshot != null){
                Games.Snapshots.commitAndClose(gameHelper.getApiClient(), snapshot, metadataChange);
                Log.d("saveGame", "Snap CommitAndClose");
            }
        }*/
	}

    private String writeSnapshot(Snapshot snapshot, byte[] d) {
        snapshot.getSnapshotContents().writeBytes(d);
        //byte[] tempCompare =  snapshot.getSnapshotContents().readFully();

        // Save the snapshot.
        SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                //.setCoverImage(getScreenShot())
                .setDescription("Modified data at: " + Calendar.getInstance().getTime())
                .build();
        Games.Snapshots.commitAndClose(gameHelper.getApiClient(), snapshot, metadataChange);
        return snapshot.toString();
    }

    private Snapshot processSnapshotOpenResult(Snapshots.OpenSnapshotResult result, int retryCount) {
        Snapshot mResolvedSnapshot = null;
        retryCount++;
        int status = result.getStatus().getStatusCode();

        Log.i("conflict", "Save Result status: " + status);

        if (status == GamesStatusCodes.STATUS_OK) {
            return result.getSnapshot();
        } else if (status == GamesStatusCodes.STATUS_SNAPSHOT_CONTENTS_UNAVAILABLE) {
            return result.getSnapshot();
        } else if (status == GamesStatusCodes.STATUS_SNAPSHOT_CONFLICT){
            Snapshot snapshot = result.getSnapshot();
            Snapshot conflictSnapshot = result.getConflictingSnapshot();

            // Resolve between conflicts by selecting the newest of the conflicting snapshots.
            mResolvedSnapshot = snapshot;

            if (snapshot.getMetadata().getLastModifiedTimestamp() <
                    conflictSnapshot.getMetadata().getLastModifiedTimestamp()){
                mResolvedSnapshot = conflictSnapshot;
            }

            Snapshots.OpenSnapshotResult resolveResult = Games.Snapshots.resolveConflict(
                    gameHelper.getApiClient(), result.getConflictId(), mResolvedSnapshot)
                    .await();

            if (retryCount < MAX_SNAPSHOT_RESOLVE_RETRIES){
                return processSnapshotOpenResult(resolveResult, retryCount);
            }else{
                String message = "Could not resolve snapshot conflicts";
                Log.e("resolve conflicts", message);
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
            }

        }
        // Fail, return null.
        return null;
    }

    @Override
	public void loadGame() {
                Log.d("loadGame","function 1 called");
//                showProgressDialog("Loading");
		if (isSignedIn()){
			AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
				@Override
				protected Integer doInBackground(Void... params) {
					// Open the saved game using its name.
					Snapshots.OpenSnapshotResult result = Games.Snapshots.open(gameHelper.getApiClient(),
							NAME_GAME_SAVED, true).await();
					Log.d("loadGame","function 2 called");

					// Check the result of the open operation
					if (result.getStatus().isSuccess()) {
						Log.d("loadGame", "Load is Succes");
						snapshot2 = result.getSnapshot();
						// Read the byte content of the saved game.
						try {
							mSaveGameData = snapshot2.getSnapshotContents().readFully();
							if (mSaveGameData == null) Log.d("saveGame","snapshot byte NULL");
							if (mSaveGameData != null){
								Log.d("loadGame", "snapshot NOT NULL");
								Log.d("loadGame size", Integer.toString(mSaveGameData.length));
							}

						} catch (IOException e) {
							Log.e("saveGame", "Error while reading Snapshot.", e);
						}
					} else{
						Log.e("saveGame", "Error while loading: " + result.getStatus().getStatusCode());
					}

					return result.getStatus().getStatusCode();
				}

				@Override
				protected void onPostExecute(Integer status) {
					// Dismiss progress dialog and reflect the changes in the UI.
					// ...
					Log.d("loadGame", "Load finished");
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							game.savedGameData = mSaveGameData;
							game.autoLoad = true;
							Log.d("loadgame", "parse byte data");
							Log.d("loadGame", Integer.toString(mSaveGameData.length));
							if (game.savedGameData.length == 0){
								Log.d("loadGame", "chamar função de criar o save");
								game.createFirstGameFile();
							}
							if (game.savedGameData.length > 0){
								game.snapToPlayerFile();
							}
							Log.d("loadGame", "resolução é: " + Integer.toString(game.checkSaveVersionConflict()));
							Log.d("loadGame", "checar versão");
						}
					});
					finishedLoad = true;
					//              dismissProgressDialog();
				}
			};

			task.execute();

		}

	}

	@Override
	public boolean checkNetworkStatus() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
            Log.d("NETWORK STATUS", "NOT CONNECTED");
			// There are no active networks.
			return false;
		} else{
            Log.d("NETWORK STATUS", "CONNECTED");
            return true;
		}

	}


	public void startImmersion(){
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
				startImmersion();
			}
		}
	}

}
