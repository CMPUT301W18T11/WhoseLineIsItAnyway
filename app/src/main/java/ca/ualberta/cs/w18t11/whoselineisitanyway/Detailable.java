package ca.ualberta.cs.w18t11.whoselineisitanyway;

import java.util.HashMap;

public interface Detailable {
    HashMap<String, String> getDetails();

    boolean hasDetailImage();
}
