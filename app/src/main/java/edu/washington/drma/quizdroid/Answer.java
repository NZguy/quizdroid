package edu.washington.drma.quizdroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Answer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Answer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Answer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "numberCorrect";
    private static final String ARG_PARAM2 = "currentQuestion";
    private static final String ARG_PARAM3 = "userAnswer";
    private static final String ARG_PARAM4 = "correctAnswer";
    private static final String ARG_PARAM5 = "isLastQuestion";

    // TODO: Rename and change types of parameters
    private int numberCorrect;
    private int currentQuestion;
    private String userAnswer;
    private String correctAnswer;
    private boolean isLastQuestion;

    private OnFragmentInteractionListener mListener;

    public Answer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param numberCorrect Parameter 1.
     * @param currentQuestion Parameter 2.
     * @return A new instance of fragment Answer.
     */
    // TODO: Rename and change types and number of parameters
    public static Answer newInstance(int numberCorrect, int currentQuestion, String userAnswer, String correctAnswer, boolean isLastQuestion) {
        Answer fragment = new Answer();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, numberCorrect);
        args.putInt(ARG_PARAM2, currentQuestion);
        args.putString(ARG_PARAM3, userAnswer);
        args.putString(ARG_PARAM4, correctAnswer);
        args.putBoolean(ARG_PARAM5, isLastQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numberCorrect = getArguments().getInt(ARG_PARAM1);
            currentQuestion = getArguments().getInt(ARG_PARAM2);
            userAnswer = getArguments().getString(ARG_PARAM3);
            correctAnswer = getArguments().getString(ARG_PARAM4);
            isLastQuestion = getArguments().getBoolean(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_answer, container, false);

        TextView textAnswer = (TextView)view.findViewById(R.id.textAnswer);
        TextView textNumberCorrect = (TextView)view.findViewById(R.id.textNumberCorrect);

        textAnswer.setText("Your answer was \"" + userAnswer + "\", and the correct answer was \"" + correctAnswer + "\"");
        textNumberCorrect.setText("You've gotten " + numberCorrect + "/" + currentQuestion + " correct");

        Button btnNext = (Button)view.findViewById(R.id.btnNext);
        if(isLastQuestion){
            btnNext.setText("Finish");
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onFinishPressed();
                    }
                }
            });
        }else{
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onNextPressed();
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        void onNextPressed();
        void onFinishPressed();
    }
}
