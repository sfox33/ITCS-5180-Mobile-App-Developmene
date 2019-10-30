package com.example.inclass03;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * Assignment #08
 * MainActivity.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */
public class EditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Student student;
    private int choice;

    public static String STUDENT_KEY = "STUDENT2";
    public static String CHOICE = "choice";

    EditText editName;
    EditText editEmail;
    String editDept;
    RadioGroup rg;
    Button saveButton;
    SeekBar editMood;
    TextView department, seekText;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton5 ) {
                    Log.d("demo", "Changing editDept to SIS");
                    editDept = "SIS";
                }
                else if (checkedId == R.id.radioButton6 ) {
                    Log.d("demo", "Changing editDept to CS");
                    editDept = "CS";
                }
                else if (checkedId == R.id.radioButton7 ) {
                    Log.d("demo", "Changing editDept to BIO");
                    editDept = "BIO";
                }
                else {
                    Log.d("demo", "Changing editDept to Others");
                    editDept = "Others";
                }
            }
        });

        if (choice == 1) {
            editEmail.setVisibility(View.GONE);
            editMood.setVisibility(View.GONE);
            rg.setVisibility(View.GONE);
            department.setVisibility(View.GONE);
            seekText.setVisibility(View.GONE);

            editName.setText(student.getName());
        }
        else if (choice == 2) {
            editName.setVisibility(View.GONE);
            editMood.setVisibility(View.GONE);
            rg.setVisibility(View.GONE);
            department.setVisibility(View.GONE);
            seekText.setVisibility(View.GONE);

            editEmail.setText(student.getEmail());
        }
        else if (choice == 3) {
            editName.setVisibility(View.GONE);
            editEmail.setVisibility(View.GONE);
            editMood.setVisibility(View.GONE);
            seekText.setVisibility(View.GONE);

            String dept = student.getDept();
            if(dept.equals("SIS")) {
                //RadioButton rb = (RadioButton)findViewById(R.id.radioButton5);
                ((RadioButton)getActivity().findViewById(R.id.radioButton5)).setChecked(true);
            } else if(dept.equals("CS")) {
                ((RadioButton)getActivity().findViewById(R.id.radioButton6)).setChecked(true);
            } else if(dept.equals("BIO")){
                ((RadioButton)getActivity().findViewById(R.id.radioButton7)).setChecked(true);
            } else if(dept.equals("Others")) {
                ((RadioButton)getActivity().findViewById(R.id.radioButton8)).setChecked(true);
            }

        }
        else {
            editName.setVisibility(View.GONE);
            editEmail.setVisibility(View.GONE);
            rg.setVisibility(View.GONE);
            department.setVisibility(View.GONE);

            int seekNum = student.getMood();
            editMood.setProgress(seekNum);
        }

        getActivity().findViewById(R.id.SaveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(choice == 1) {
                    String val = editName.getText().toString();
                    if(val.equals("")) {
                        //setResult(RESULT_CANCELED);
                    } else {
                       student.setName(val);
                       mListener.passToMain(student);
                       mListener.onFragmentInteraction(0);
                    }
                } else if(choice == 2) {
                    String val = editEmail.getText().toString();
                    if(!validateEmail(val)){
                        Toast.makeText(getContext().getApplicationContext(), "Enter valid email", Toast.LENGTH_SHORT).show();
                    }else if(val.equals("")) {
                        //setResult(RESULT_CANCELED);
                    } else {
                        student.setEmail(val);
                        mListener.passToMain(student);
                        mListener.onFragmentInteraction(0);
                    }

                } else if(choice == 3) {
                    String val = editDept;
                    Log.d("demo", "Department is supposed to be " + val);
                    if(val.equals("")) {
                       // setResult(RESULT_CANCELED);
                    } else {
                        student.setDept(val);
                        mListener.passToMain(student);
                        mListener.onFragmentInteraction(0);
                    }
                } else if(choice == 4) {
                    int val = editMood.getProgress();
                    if (val < 0 || val > editMood.getMax()) {
                        //setResult(RESULT_CANCELED);
                    } else {
                        student.setMood(val);
                        mListener.passToMain(student);
                        mListener.onFragmentInteraction(0);
                    }
                }
            }
        });
    }

    private boolean validateEmail(String email){
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private OnFragmentInteractionListener mListener;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
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
        choice = bundle.getInt(this.CHOICE);
        student = (Student) bundle.getSerializable(this.STUDENT_KEY);

        Log.d("Demo", "student name: " + student.getName());
        Log.d("Demo", "student email: " + student.getEmail());
        Log.d("Demo", "student dep: " + student.getDept());
        Log.d("Demo", "student mood: " + student.getMood());
        Log.d("Demo", "choice : " + choice);



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        rg = (RadioGroup) view.findViewById(R.id.Group2);
        saveButton = (Button)view.findViewById(R.id.button2);
        editMood = (SeekBar)view.findViewById(R.id.EditMood);
        editName = (EditText) view.findViewById(R.id.EditName);
        editEmail = (EditText) view.findViewById(R.id.EditEmail);
        department = (TextView) view.findViewById(R.id.EditDept);
        seekText = (TextView) view.findViewById(R.id.EditMoodText);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
