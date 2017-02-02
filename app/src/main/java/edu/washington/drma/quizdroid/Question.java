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
import android.widget.RadioButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Question.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Question#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Question extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "isLastQuestion";

    // TODO: Rename and change types of parameters
    //private boolean isLastQuestion;

    private OnFragmentInteractionListener mListener;
    String userAnswer;
    final String correctAnswer = "RadioButton3";
    Button btnSubmit;

    public Question() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment Question.
     */
    // TODO: Rename and change types and number of parameters
    public static Question newInstance() {
        Question fragment = new Question();
        Bundle args = new Bundle();
        //args.putBoolean(ARG_PARAM1, isLastQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //isLastQuestion = getArguments().getBoolean(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        // How can I make a listener that listens to all buttons in a fragment?
        RadioButton radioButton1 = (RadioButton)view.findViewById(R.id.radioButton1);
        RadioButton radioButton2 = (RadioButton)view.findViewById(R.id.radioButton2);
        RadioButton radioButton3 = (RadioButton)view.findViewById(R.id.radioButton3);
        RadioButton radioButton4 = (RadioButton)view.findViewById(R.id.radioButton4);

        radioButton1.setOnClickListener(new RadioListener());
        radioButton2.setOnClickListener(new RadioListener());
        radioButton3.setOnClickListener(new RadioListener());
        radioButton4.setOnClickListener(new RadioListener());

        btnSubmit = (Button) view.findViewById(R.id.btnSumbit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSubmitPressed(userAnswer, correctAnswer);
                }
            }
        });



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
        void onSubmitPressed(String userAnswer, String correctAnswer);
    }

    public class RadioListener implements View.OnClickListener{

        public RadioListener(){

        }

        public void onClick(View v){
            // Is the button now checked?
            userAnswer = ((RadioButton) v).getText().toString();
            btnSubmit.setVisibility(View.VISIBLE);
        }

    }
}
