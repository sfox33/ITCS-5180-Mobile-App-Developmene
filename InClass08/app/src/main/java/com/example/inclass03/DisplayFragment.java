package com.example.inclass03;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * Assignment #08
 * MainActivity.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */
public class DisplayFragment extends Fragment {

    ImageButton button1, button2, button3, button4;
    TextView nameText;
    TextView emailText;
    TextView deptText;
    TextView moodText;

    public static String STUDENT_KEY = "STUDENT2";

    private Student student;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayFragment newInstance(String param1, String param2) {
        DisplayFragment fragment = new DisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        student = (Student) bundle.getSerializable(this.STUDENT_KEY);
        Log.d("Demo", "student name: " + student.getName());
        Log.d("Demo", "student name: " + student.getEmail());
        Log.d("Demo", "student name: " + student.getDept());
        Log.d("Demo", "student name: " + student.getMood());


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nameText.setText(student.getName());
        emailText.setText(student.getEmail());
        deptText.setText(student.getDept());
        moodText.setText(String.valueOf(student.getMood()) + "% Positive");

        getActivity().findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.passToMain(student);
                mListener.onFragmentInteraction(1);
            }
        });

        getActivity().findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.passToMain(student);
                mListener.onFragmentInteraction(2);
            }
        });

        getActivity().findViewById(R.id.imageButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.passToMain(student);
                mListener.onFragmentInteraction(3);
            }
        });

        getActivity().findViewById(R.id.imageButton4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.passToMain(student);
                mListener.onFragmentInteraction(4);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view  = inflater.inflate(R.layout.fragment_display, container, false);

        button1 = (ImageButton) view.findViewById(R.id.imageButton);
        button2 = (ImageButton) view.findViewById(R.id.imageButton2);
        button3 = (ImageButton) view.findViewById(R.id.imageButton3);
        button4 = (ImageButton) view.findViewById(R.id.imageButton4);

        nameText = (TextView) view.findViewById(R.id.displayName);
        emailText = (TextView) view.findViewById(R.id.displayEmail);
        deptText = (TextView) view.findViewById(R.id.displayDept);
        moodText = (TextView) view.findViewById(R.id.displayMood);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(0);
        }
    }*/

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
        void onFragmentInteraction(int index);

        void passToMain(Student student);

    }
}
