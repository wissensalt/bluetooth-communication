package com.tripoin.bluetooh.communication;

/**
 * Created on 12/19/17.
 *
 * @author <a href="mailto:fauzi.knightmaster.achmad@gmail.com">Achmad Fauzi</a>
 */
public class Main {

    public static void main(String [] p_Args) {
        ISearchService searchService = new SearchServiceImpl();
        searchService.searchDevices();
        /*try {
            new LibJExample().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
