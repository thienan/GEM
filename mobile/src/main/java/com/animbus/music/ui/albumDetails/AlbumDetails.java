package com.animbus.music.ui.albumDetails;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.animbus.music.R;
import com.animbus.music.customImpls.ThemableActivity;
import com.animbus.music.data.adapter.AlbumDetailsAdapter;
import com.animbus.music.media.MediaData;
import com.animbus.music.media.PlaybackManager;
import com.animbus.music.media.objects.Album;
import com.animbus.music.media.objects.Song;
import com.animbus.music.ui.nowPlaying.NowPlaying;
import com.animbus.music.ui.settings.Settings;
import com.animbus.music.ui.theme.Theme;

import java.util.List;

public class AlbumDetails extends ThemableActivity {
    static final int intNull = -1;
    private static final int COLOR_DELAY_BASE = 550;
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsingToolbar;
    RecyclerView mList;
    FloatingActionButton mFAB;
    Album mAlbum;
    Toolbar mDetails;
    boolean tempFavorite = false;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_album_details);
        mAlbum = MediaData.get().findAlbumById(getIntent().getLongExtra("album_id", -1));
    }

    @Override
    protected void setVariables() {
        mToolbar = (Toolbar) findViewById(R.id.album_details_toolbar);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.album_details_collapsing_toolbar);
        mList = (RecyclerView) findViewById(R.id.album_details_recycler);
        mFAB = (FloatingActionButton) findViewById(R.id.album_details_fab);
        mDetails = (Toolbar) findViewById(R.id.album_details_info_toolbar);
    }

    @Override
    protected void setUp() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        configureFab();
        configureRecyclerView();
        configureUI();
    }

    private void configureRecyclerView() {
        AlbumDetailsAdapter adapter = new AlbumDetailsAdapter(this, mAlbum.getSongs());
        mList.setAdapter(adapter);
        adapter.setOnItemClickedListener(new AlbumDetailsAdapter.AlbumDetailsClickListener() {
            @Override
            public void onAlbumDetailsItemClicked(View v, List<Song> data, int pos) {
                PlaybackManager.get().play(data, pos);
            }
        });
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void configureFab() {
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackManager.get().play(mAlbum.getSongs(), 0);
                transitionNowPlaying();
            }
        });
    }

    private void configureUIColors() {
        FabHelper.setFabBackground(mFAB, mAlbum.accentColor);
        FabHelper.setFabTintedIcon(mFAB, getResources().getDrawable(R.drawable.ic_play_arrow_black_48dp), mAlbum.accentIconColor);
        mDetails.setBackgroundColor(mAlbum.BackgroundColor);
        mDetails.setTitleTextColor(mAlbum.TitleTextColor);
        mDetails.setSubtitleTextColor(mAlbum.SubtitleTextColor);
        mCollapsingToolbar.setContentScrimColor(mAlbum.BackgroundColor);
        mCollapsingToolbar.setStatusBarScrimColor(mAlbum.BackgroundColor);
    }

    private void configureUI() {
        ImageView mImage = (ImageView) findViewById(R.id.album_details_album_art);
        mImage.setImageBitmap(mAlbum.getAlbumArt());
        mDetails.setTitle(mAlbum.getAlbumTitle());
        mCollapsingToolbar.setTitle(mAlbum.getAlbumTitle());
        mDetails.setSubtitle(mAlbum.getAlbumArtistName());
        findViewById(R.id.album_details_favorite_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLikeClicked((ImageView) v);
            }
        });
        configureUIColors();
    }

    private void onLikeClicked(ImageView v){
        Toast.makeText(this, R.string.msg_coming_soon, Toast.LENGTH_SHORT).show();
    }

    private void transitionNowPlaying() {
        startActivity(new Intent(this, NowPlaying.class));
    }

    @Override
    protected void setUpTheme(Theme theme) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, Settings.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
