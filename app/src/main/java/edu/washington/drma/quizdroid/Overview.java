package edu.washington.drma.quizdroid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Overview.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Overview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Overview extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_QUIX_INDEX = "drma.quizIndex";

    private int quizIndex;

    private OnFragmentInteractionListener mListener;

    // Data
    String[][] quizOverviewData = new String[][]{
            {"Math", "5"},
            {"Physics", "2"},
            {"Marvel", "2"}
    };

    public Overview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param quizIndex Parameter 1.
     * @return A new instance of fragment Overview.
     */
    // TODO: Rename and change types and number of parameters
    public static Overview newInstance(int quizIndex) {
        Overview fragment = new Overview();
        Bundle args = new Bundle();
        args.putInt(ARG_QUIX_INDEX, quizIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizIndex = getArguments().getInt(ARG_QUIX_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        String[] currentQuiz = quizOverviewData[quizIndex];

        TextView textTitle = (TextView)view.findViewById(R.id.textTitle);
        TextView textDescr = (TextView)view.findViewById(R.id.textDescr);
        TextView textQuestions = (TextView)view.findViewById(R.id.textQuestions);

        textTitle.setText(currentQuiz[0] + " Overview");
        textDescr.setText("This quiz contains many questions about " + currentQuiz[0]);
        textQuestions.setText(currentQuiz[1] + " questions");

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
