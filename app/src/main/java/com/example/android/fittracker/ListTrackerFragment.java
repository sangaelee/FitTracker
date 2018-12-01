package com.example.android.fittracker;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.android.fittracker.data.FittrackerTableHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListTrackerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        FittrackerAdapter.ClickListener{
    private static final String TAG = ListTrackerFragment.class.getSimpleName();
    private final static int LOADER_ID = 0;
    RecyclerView mRecyclerView;
    FittrackerAdapter mAdapter;
    FragmentManager fm;
    Cursor mCursor;
    long mSelectId;
    private static final int DELETED_ID = Menu.FIRST + 1;

    public ListTrackerFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_tracker, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager linearManager= new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        fm = getFragmentManager();
        //init loader
        getLoaderManager().initLoader(LOADER_ID, null, this);
        registerForContextMenu(mRecyclerView);
        return view;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                FittrackerTableHandler.FittrackerID,
                FittrackerTableHandler.Date,
                FittrackerTableHandler.Type,
                FittrackerTableHandler.Distance,
                FittrackerTableHandler.Duration,
                FittrackerTableHandler.Calorie
        };

        return new CursorLoader(getContext(), MyContentProvider.CONTENT_URI, projection, null, null, FittrackerTableHandler.Date + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        mCursor.moveToFirst();
        mAdapter = new FittrackerAdapter(getContext(), cursor, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setData(null);
    }



    public void onItemClicked(View view, int position) {
        mCursor.moveToPosition(position);

        long id = -1;
        if (mCursor.getColumnIndex(FittrackerTableHandler.FittrackerID) >= 0) {
            id = mCursor.getLong(mCursor.getColumnIndex(FittrackerTableHandler.FittrackerID));
        }
        Bundle arguments = new Bundle();
        arguments.putLong(EditTrackerFragment.ARG_ID, id);
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
        arguments.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE, uri);
        Fragment fragment = null;
        fragment = new EditTrackerFragment();
        fragment.setArguments(arguments);
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }


    public boolean onItemLongClicked(View view, int position) {
        mCursor.moveToPosition(position);

        if (mCursor.getColumnIndex(FittrackerTableHandler.FittrackerID) >= 0) {
            mSelectId = mCursor.getLong(mCursor.getColumnIndex(FittrackerTableHandler.FittrackerID));
        }
        PopupMenu popup = new PopupMenu(getActivity(), view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.context_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + mSelectId);
                getContext().getContentResolver().delete(uri, null, null);
                return true;
            }
        });

        popup.show();//showing popup menu
        return true;
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        Log.i(TAG, "SangaeLee(147948186) onCreateContextMenu");
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        menu.setHeaderTitle("Select The Action");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i(TAG, "SangaeLee(147948186) onContextItemSelected");
        if (item.getItemId() == R.id.menu_delete) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + info.id);
            getContext().getContentResolver().delete(uri, null, null);
            Toast.makeText(getContext(), "Address is deleted.", Toast.LENGTH_SHORT).show();
            //fillData();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
