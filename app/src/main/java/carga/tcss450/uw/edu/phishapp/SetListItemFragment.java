package carga.tcss450.uw.edu.phishapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import carga.tcss450.uw.edu.phishapp.setlist.SetList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSetListItemFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SetListItemFragment extends Fragment {

    private OnSetListItemFragmentInteractionListener mListener;

    public SetListItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_set_list_item, container, false);

        Button b = v.findViewById(R.id.btn_fragment_setlist_item_fulltext);
        b.setOnClickListener(this::loadFullText);

        return v;
    }

    private void loadFullText(View view) {
        SetList setList = (SetList) getArguments().getSerializable(getString(R.string.key_setlist_object));

        Uri uri = Uri.parse(setList.getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            SetList post = (SetList) getArguments().getSerializable(getString(R.string.key_setlist_object));
            updateContent(post);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSetListItemFragmentInteractionListener) {
            mListener = (OnSetListItemFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateContent(SetList item) {

        TextView textViewLongDate = getActivity().findViewById(R.id.textview_fragment_setlist_item_longdate);
        TextView textViewLocation = getActivity().findViewById(R.id.textview_fragment_setlist_item_location);
        TextView textViewSetListData = getActivity().findViewById(R.id.textview_fragment_setlist_item_setlist_data);
        TextView textViewSetListNotes = getActivity().findViewById(R.id.textview_fragment_setlist_item_setlist_notes);

        textViewLongDate.setText(item.getLongDate());
        textViewLocation.setText(Html.fromHtml(item.getLocation(), Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE,
                null,
                null));
        textViewSetListData.setText(Html.fromHtml(item.getSetListData(), Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE,
                null,
                null));
        textViewSetListNotes.setText(Html.fromHtml(item.getSetListNotes(),
                Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE,
                null,
                null));


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSetListItemFragmentInteractionListener {
        void onSetListButtonFragmentInteraction(View view);
    }
}
