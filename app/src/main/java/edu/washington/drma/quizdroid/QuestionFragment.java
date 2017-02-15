package edu.washington.drma.quizdroid;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "topicIndex";
    private static final String ARG_PARAM2 = "questionIndex";

    // TODO: Rename and change types of parameters
    private int topicIndex;
    private int questionIndex;

    private OnFragmentInteractionListener mListener;
    int userAnswer;
    Button btnSubmit;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(int topicIndex, int questionIndex) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, topicIndex);
        args.putInt(ARG_PARAM2, questionIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            topicIndex = getArguments().getInt(ARG_PARAM1);
            questionIndex = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        QuizApp app = (QuizApp)getActivity().getApplication();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        // Set question text
        TextView qText = (TextView)view.findViewById(R.id.questionText);
        qText.setText(app.getRepository().getQuestionText(topicIndex, questionIndex));

        // Populate list of radio buttons
        RadioGroup rGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        LinearLayout.LayoutParams radioLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        String[] questionAnswers = app.getRepository().getQuestionAnswers(topicIndex, questionIndex);
        for(int i = 0; i < questionAnswers.length; i++){
            RadioButton rButton = new RadioButton(getActivity());
            rButton.setLayoutParams(radioLayout);
            rButton.setTag(i);
            rButton.setText(questionAnswers[i]);
            rButton.setOnClickListener(new RadioListener());
            rGroup.addView(rButton);
        }

        // Create listener for submit button
        btnSubmit = (Button) view.findViewById(R.id.btnSumbit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    QuizApp app = (QuizApp)getActivity().getApplication();
                    app.getRepository().setQuestionUserAnswer(topicIndex, questionIndex, userAnswer);
                    mListener.onSubmitPressed();
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
        void onSubmitPressed();
    }

    public class RadioListener implements View.OnClickListener{

        public RadioListener(){

        }

        public void onClick(View v){
            // Is the button now checked?
            //userAnswer = ((RadioButton) v).getText().toString();
            userAnswer = Integer.parseInt(v.getTag().toString());
            btnSubmit.setVisibility(View.VISIBLE);
        }

    }
}
