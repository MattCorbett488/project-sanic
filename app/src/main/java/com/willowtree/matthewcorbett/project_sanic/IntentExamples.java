package com.willowtree.matthewcorbett.project_sanic;

import android.content.Intent;

public class IntentExamples {

    private static final String EXAMPLE_INTEGER_KEY = "INT_KEY";
    private static final String EXAMPLE_CHARACTER_KEY = "CHAR_KEY";
    private static final String EXAMPLE_BOOLEAN_KEY = "BOOL_KEY";
    private static final String EXAMPLE_STRING_KEY = "STRING_KEY";
    private static final String EXAMPLE_ARRAY_KEY = "ARRAY_KEY";

    /*
    An example method for how to put extras into an Intent
     */
    public void putExtrasExamples() {
        Intent intent = new Intent();

        int intExample = 12;
        intent.putExtra(EXAMPLE_INTEGER_KEY, intExample);

        char charExample = 'c';
        intent.putExtra(EXAMPLE_CHARACTER_KEY, charExample);

        boolean boolExample = true;
        intent.putExtra(EXAMPLE_BOOLEAN_KEY, boolExample);

        String stringExample = "example";
        intent.putExtra(EXAMPLE_STRING_KEY, stringExample);

        short[] arrayExample = new short[2];
        intent.putExtra(EXAMPLE_ARRAY_KEY, arrayExample);
    }

    /*
    An example method for how to extract extras from an Intent
     */
    public void getExtrasExamples() {
        Intent intent = new Intent();

        //We can get the Bundle directly if we want, but don't need to
        intent.getExtras();

        //Get an integer and specify -1 as the default value if the key has no associated value
        intent.getIntExtra(EXAMPLE_INTEGER_KEY, -1);

        //Get a character and specify 'a' as the default value if the key has no associated value
        intent.getCharExtra(EXAMPLE_CHARACTER_KEY, 'a');

        //Get a boolean and specify false as the default value if the key has no associated value
        intent.getBooleanExtra(EXAMPLE_BOOLEAN_KEY, false);

        //Get a String; if the key has no associated value then it returns null
        intent.getStringExtra(EXAMPLE_STRING_KEY);

        /*
        Get a Short array in this instance - there are methods for every type of array
         */
        intent.getShortArrayExtra(EXAMPLE_ARRAY_KEY);
    }
}
