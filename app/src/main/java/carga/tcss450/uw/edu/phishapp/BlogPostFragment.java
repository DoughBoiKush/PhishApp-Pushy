package carga.tcss450.uw.edu.phishapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import carga.tcss450.uw.edu.phishapp.blog.BlogPost;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnBlogPostFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BlogPostFragment extends Fragment {

    private OnBlogPostFragmentInteractionListener mListener;

    public BlogPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_blog_post, container, false);

        Button b = v.findViewById(R.id.btn_fragment_blog_post_full_text);
        b.setOnClickListener(this::loadFullPost);

        return v;
    }

    private void loadFullPost(View view) {

        BlogPost post = (BlogPost) getArguments().getSerializable(getString(R.string.key_blog_post_object));

        Uri uri = Uri.parse(post.getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBlogPostFragmentInteractionListener) {
            mListener = (OnBlogPostFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBlogPostFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            BlogPost post = (BlogPost) getArguments().getSerializable(getString(R.string.key_blog_post_object));
            updateContent(post);
        }
    }

    public void updateContent(BlogPost post) {
        TextView textViewTitle = getActivity().findViewById(R.id.textView_fragment_blog_post_blog_title);
        TextView textViewDate = getActivity().findViewById(R.id.textView_fragment_blog_post_post_date);
        TextView textViewTeaser = getActivity().findViewById(R.id.textView_fragment_blog_post_full_teaser);

        textViewTitle.setText(post.getTitle());
        textViewDate.setText(post.getPubDate());
        textViewTeaser.setText(Html.fromHtml(post.getTeaser(), Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE,
                null, null));

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
    public interface OnBlogPostFragmentInteractionListener {
        void onBlogPostFragmentInteraction(View view);
    }


}
