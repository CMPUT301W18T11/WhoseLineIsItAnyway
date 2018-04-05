package ca.ualberta.cs.w18t11.whoselineisitanyway.model.constants;

import android.support.annotation.NonNull;

/**
 * Contains constant values used through the project.
 *
 * @author Samuel Dolha
 * @version 1.0
 */
public final class Constants
{
    /**
     * The base URL used to connect to Elasticsearch.
     */
    @NonNull
    public static final String ELASTICSEARCH_URL = "http://cmput301.softwareprocess.es:8080";

    /**
     * The name of the project's Elasticsearch index.
     */
    @NonNull
    public static final String ELASTICSEARCH_INDEX = "cmput301w18t11";

    private Constants()
    {
        // Do nothing here.
    }
}
