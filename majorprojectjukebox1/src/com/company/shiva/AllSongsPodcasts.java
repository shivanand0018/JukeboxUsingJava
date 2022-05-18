package com.company.shiva;

import java.sql.*;
import java.util.Scanner;

public class AllSongsPodcasts {
    public static void displayAllSongs(String user_id) {
        Scanner scan = new Scanner(System.in);
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox", "root", "password@123");
            Statement st = con.createStatement();
            int choice;
            do {
                System.out.println("Please enter your choice:\n1.Songs,\n2.Podcasts,\n3.View Playlist,\n4.Exit");
                choice = scan.nextInt();
                switch (choice) {
                    case 1:int opt;
                        do {
                            System.out.println("Select your choice:\n1.Display All Songs,\n2.Search Songs,\n3.Back");
                            opt = scan.nextInt();
                            switch (opt) {
                                case 1:
                                    ResultSet rs = st.executeQuery("select song_id,name,filepath from song");
                                    System.out.println("*****----Songs-----*****");
                                    System.out.format("%10s %15s","Song Id","Song Name");
                                    System.out.println("");
                                    while (rs.next()) {
                                        System.out.format("%10d %15s", rs.getInt(1), rs.getString(2));
                                        System.out.println("");
                                    }
                                    System.out.println("Please enter Song id");
                                    int opt1 = scan.nextInt();
                                    ResultSet rs1 = st.executeQuery("select filepath from song where song_id=" + opt1);
                                    while (rs1.next()) {
                                        SimpleAudioPlayer.play(rs1.getString(1));
                                    }
                                    System.out.println("Do you want to add this song for your playlist:\n1.yes,\n2.no");
                                    int opt3= scan.nextInt();
                                    if(opt3==1)
                                    {
                                        System.out.println("Select your Choice:\n1.Add to new Playlist,\n2.Existing playlist");
                                        int opt5= scan.nextInt();
                                        if(opt5==1){
                                            PreparedStatement st2 = con.prepareStatement("insert into playlist(name,userid) values(?,?)");
                                            System.out.println("Give a name for playlist");
                                            scan.nextLine();
                                            String playlistName = scan.nextLine();
                                            st2.setString(1, playlistName);
                                            st2.setString(2, user_id);
                                            int i = st2.executeUpdate();
                                            ResultSet rs4 = st.executeQuery("select playlist_id from playlist where userid=" + user_id+" and playlist.name='"+playlistName+"'");
                                            int playlistid = 0;
                                            while (rs4.next()) {
                                                playlistid = rs4.getInt(1);
                                            }
                                            PreparedStatement st3 = con.prepareStatement("insert into playlist_song_details(playlist_id,song_id) values(?,?)");
                                            st3.setInt(1, playlistid);
                                            st3.setInt(2, opt1);
                                            i = st3.executeUpdate();
                                            if(i>0) {
                                                System.out.println("Successfully added to your Playlist");
                                            }
                                            ResultSet rs6=st.executeQuery("select * from user join playlist join playlist_song_details join song where user.userid=playlist.userid and playlist.playlist_id=playlist_song_details.playlist_id and song.song_id=playlist_song_details.song_id and user.userid='"+user_id+"' and playlist.playlist_id="+playlistid);
                                            System.out.println("****Songs in your Playlist****");
                                            System.out.format("%10s %15s", "Song Id", "Song Name");
                                            System.out.println("");
                                            while (rs6.next()) {
                                                System.out.format("%10d %10s", rs6.getInt(12), rs6.getString(13));
                                                System.out.println("");
                                            }
                                        }
                                        if(opt5==2)
                                        {
                                            System.out.println("Existing Playlists");
                                            ResultSet rs6=st.executeQuery("select * from playlist where userid='"+user_id+"'");
                                            int j=0;
                                            while (rs6.next())
                                            {
                                                System.out.format("%d %s",rs6.getInt(1),rs6.getString(2));
                                                System.out.println("");
                                                j++;
                                            }
                                            if(j==0)
                                            {
                                                System.out.println("No existing playlists");
                                                break;
                                            }
                                            ResultSet rs7=st.executeQuery("select * from playlist_song_details join playlist where playlist.userid='"+user_id+"' and song_id="+opt1);
                                            if(rs7.next())
                                            {
                                                System.out.println("Song already exists in your playlist");
                                                break;
                                            }
                                            System.out.println("Please select a playlist Id");
                                            int choice2= scan.nextInt();
                                            PreparedStatement st3 = con.prepareStatement("insert into playlist_song_details(playlist_id,song_id) values(?,?)");
                                            st3.setInt(1,choice2);
                                            st3.setInt(2, opt1);
                                            int i= st3.executeUpdate();
                                            if(i>0)
                                            {
                                                System.out.println("Successfully added to your playlist");
                                            }
                                            ResultSet rs20=st.executeQuery("select * from user join playlist join playlist_song_details join song where user.userid=playlist.userid and playlist.playlist_id=playlist_song_details.playlist_id and song.song_id=playlist_song_details.song_id and user.userid='"+user_id+"' and playlist.playlist_id="+choice2);
                                            System.out.println("****Songs in your Playlist****");
                                            System.out.format("%10s %15s", "Song Id", "Song Name");
                                            System.out.println("");
                                            while (rs6.next()) {
                                                System.out.format("%10d %10s", rs20.getInt(12), rs20.getString(13));
                                                System.out.println("");
                                            }
                                        }
                                    }
                                    break;
                                case 2:
                                    System.out.println("Please select :\n1.Search by Song Name,\n2.Search by Artist name,\n3.Search by Album Name:");
                                    int opt7=scan.nextInt();
                                    ResultSet rs3 = null;
                                    switch (opt7)
                                    {
                                        case 1:
                                            System.out.println("Please enter song Name:");
                                            scan.nextLine();
                                            String ans=scan.nextLine();
                                            rs3 = st.executeQuery("select * from song join album join artist where song.artist_id=artist.artist_id and song.album_id=album.album_id and song.name='"+ans+"'");
                                            break;
                                        case 2:
                                            System.out.println("Please enter Artist Name:");
                                            scan.nextLine();
                                            ans=scan.nextLine();
                                            rs3 = st.executeQuery("select * from song join album join artist where song.artist_id=artist.artist_id and song.album_id=album.album_id and artist.name='"+ans+"'");
                                            break;
                                        case 3:
                                            System.out.println("Please enter Album Name:");
                                            scan.nextLine();
                                            ans=scan.nextLine();
                                            rs3 = st.executeQuery("select * from song join album join artist where song.artist_id=artist.artist_id and song.album_id=album.album_id and album.album_name='"+ans+"'");
                                            break;
                                    }
                                    System.out.println("Search Results:");
                                    System.out.format("%10s %10s %s %s","Song Id","Song Name","Album Name","Artist Name");
                                    System.out.println("");
                                    int j=0;
                                    while (rs3.next()) {
                                        System.out.format("%10d %10s %10s %10s ", rs3.getInt(1), rs3.getString(2), rs3.getString(9), rs3.getString(12));
                                        System.out.println("");
                                        j++;
                                    }
                                    if(j==0)
                                    {
                                        System.out.println("No songs available");
                                        break;
                                    }
                                    System.out.println("Please enter Song Id");
                                    int opt2 = scan.nextInt();
                                    ResultSet rs4 = st.executeQuery("select filepath from song where song_id=" + opt2);
                                    while (rs4.next()) {
                                        SimpleAudioPlayer.play(rs4.getString(1));
                                    }
                                    System.out.println("Do you want to add this song for your playlist:\n1.yes,\n2.no");
                                    int opt10= scan.nextInt();
                                    if(opt10==1) {
                                        System.out.println("Select your Choice:\n1.Add to new Playlist,\n2.Existing playlist");
                                        int opt11 = scan.nextInt();
                                        if (opt11 == 1) {
                                            PreparedStatement st7 = con.prepareStatement("insert into playlist(name,userid) values(?,?)");
                                            System.out.println("Give a name for playlist");
                                            scan.nextLine();
                                            String playlistName = scan.nextLine();
                                            st7.setString(1, playlistName);
                                            st7.setString(2, user_id);
                                            int i = st7.executeUpdate();
                                            ResultSet rs7 = st.executeQuery("select playlist_id from playlist where userid=" + user_id);
                                            int playlistid = 0;
                                            while (rs7.next()) {
                                                playlistid = rs7.getInt(1);
                                            }
                                            PreparedStatement st8 = con.prepareStatement("insert into playlist_song_details(playlist_id,song_id) values(?,?)");
                                            st8.setInt(1, playlistid);
                                            st8.setInt(2, opt11);
                                            i = st8.executeUpdate();
                                            if (i > 0) {
                                                System.out.println("Successfully added to your Playlist");
                                            }
                                            ResultSet rs6=st.executeQuery("select * from user join playlist join playlist_song_details join song where user.userid=playlist.userid and playlist.playlist_id=playlist_song_details.playlist_id and song.song_id=playlist_song_details.song_id and user.userid='"+user_id+"' and playlist.playlist_id="+playlistid);
                                            System.out.println("****Songs in your Playlist****");
                                            System.out.format("%10s %15s", "Song Id", "Song Name");
                                            System.out.println("");
                                            while (rs6.next()) {
                                                System.out.format("%10d %10s", rs6.getInt(12), rs6.getString(13));
                                                System.out.println("");
                                            }
                                        }
                                        if (opt11 == 2)
                                        {
                                            System.out.println("Existing Playlists");
                                            ResultSet rs6 = st.executeQuery("select * from playlist where userid='" + user_id + "'");
                                            int k=0;
                                            while (rs6.next()) {
                                                System.out.format("%d %s", rs6.getInt(1), rs6.getString(2));
                                                System.out.println("");
                                                k++;
                                            }
                                            if(k==0)
                                            {
                                                System.out.println("No existing playlists");
                                                break;
                                            }
                                            ResultSet rs7 = st.executeQuery("select * from playlist_song_details join playlist where playlist.userid='" + user_id + "' and song_id=" + opt2);
                                            if (rs7.next()) {
                                                System.out.println("Song already exists in your playlist");
                                                break;
                                            }
                                            System.out.println("Please select a playlist Id");
                                            int choice2 = scan.nextInt();
                                            PreparedStatement st3 = con.prepareStatement("insert into playlist_song_details(playlist_id,song_id) values(?,?)");
                                            st3.setInt(1, choice2);
                                            st3.setInt(2, opt2);
                                            int i = st3.executeUpdate();
                                            if (i > 0) {
                                                System.out.println("Successfully added to your playlist");
                                            }
                                            ResultSet rs19=st.executeQuery("select * from user join playlist join playlist_song_details join song where user.userid=playlist.userid and playlist.playlist_id=playlist_song_details.playlist_id and song.song_id=playlist_song_details.song_id and user.userid='"+user_id+"' and playlist.playlist_id="+choice2);
                                            System.out.println("****Songs in your Playlist****");
                                            System.out.format("%10s %15s", "Song Id", "Song Name");
                                            System.out.println("");
                                            while (rs6.next()) {
                                                System.out.format("%10d %10s", rs19.getInt(12), rs19.getString(13));
                                                System.out.println("");
                                            }
                                        }
                                    }
                                    break;
                                case 3:break;
                            }
                        }while (opt!=3);
                        break;

                    case 2:int opt10;
                        do {
                            System.out.println("Select your choice:\n1.Display All Podcasts,\n2.Search Podcasts,\n3.Back");
                            opt10 = scan.nextInt();
                            switch (opt10) {
                                case 1:
                                    ResultSet rs = st.executeQuery("select * from podcast");
                                    System.out.println("*****----Podcasts-----*****");
                                    System.out.format("%10s %15s %10s %15s %10s", "Podcast Id", "Podcast Name", "Language", "Release date", "Celebrity Name");
                                    System.out.println("");
                                    while (rs.next()) {
                                        System.out.format("%10d %15s %10s %15s %10s", rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                                        System.out.println("");
                                    }
                                    System.out.println("Please select Podcast Id");
                                    opt = scan.nextInt();
                                    System.out.format("%10s %15s %10s %15s %10s", "Podcast Id", "Episode Id", "Episode Name", "On air date", "Celebrity Name");
                                    System.out.println("");
                                    ResultSet rs1 = st.executeQuery("select * from podcast join podcast_episodes where podcast.podcast_id=podcast_episodes.podcast_id and podcast.podcast_id=" + opt);
                                    while (rs1.next()) {
                                        System.out.format("%10d %15d %10s %15s %10s", rs1.getInt(1), rs1.getInt(6), rs1.getString(8), rs1.getString(9), rs1.getString(5));
                                        System.out.println("");
                                    }
                                    System.out.println("Please select episode Id:");
                                    int opt2 = scan.nextInt();
                                    ResultSet rs9 = st.executeQuery("select filepath from podcast_episodes where episode_id=" + opt2);
                                    while (rs9.next()) {
                                        SimpleAudioPlayer.play(rs9.getString(1));
                                    }
                                    System.out.println("Do you want to add this podcast for your playlist:\n1.yes,\n2.no");
                                    int opt3 = scan.nextInt();
                                    if (opt3 == 1) {
                                        System.out.println("Select your Choice:\n1.Add to new Playlist,\n2.Existing playlist");
                                        int opt5 = scan.nextInt();
                                        if (opt5 == 1) {
                                            PreparedStatement st2 = con.prepareStatement("insert into playlist(name,userid) values(?,?)");
                                            System.out.println("Give a name for playlist");
                                            scan.nextLine();
                                            String playlistName = scan.nextLine();
                                            st2.setString(1, playlistName);
                                            st2.setString(2, user_id);
                                            int i = st2.executeUpdate();
                                            ResultSet rs4 = st.executeQuery("select playlist_id from playlist where userid=" + user_id);
                                            int playlistid = 0;
                                            while (rs4.next()) {
                                                playlistid = rs4.getInt(1);
                                            }
                                            PreparedStatement st3 = con.prepareStatement("insert into playlist_podcast_details(playlist_id,podcast_id) values(?,?)");
                                            st3.setInt(1, playlistid);
                                            st3.setInt(2, opt);
                                            i = st3.executeUpdate();
                                            if (i > 0) {
                                                System.out.println("Successfully added to your Playlist");
                                            }
                                        }
                                        if (opt5 == 2) {
                                            System.out.println("Existing Playlists");
                                            ResultSet rs6 = st.executeQuery("select * from playlist where userid='" + user_id + "'");
                                            int j = 0;
                                            while (rs6.next()) {
                                                System.out.format("%d %s", rs6.getInt(1), rs6.getString(2));
                                                System.out.println("");
                                                j++;
                                            }
                                            if (j == 0) {
                                                System.out.println("No existing playlists");
                                                break;
                                            }
                                            ResultSet rs7 = st.executeQuery("select * from playlist_podcast_details join podcast_episodes join playlist where playlist_podcast_details.playlist_id=playlist.playlist_id and playlist.userid='" + user_id + "' and podcast_episodes.podcast_id=" + opt);
                                            if (rs7.next()) {
                                                System.out.println("Podcast already exists in your playlist");
                                                break;
                                            }
                                            System.out.println("Please select a playlist Id");
                                            int choice2 = scan.nextInt();
                                            PreparedStatement st3 = con.prepareStatement("insert into playlist_podcast_details(playlist_id,podcast_id) values(?,?)");
                                            st3.setInt(1, choice2);
                                            st3.setInt(2, opt);
                                            int i = st3.executeUpdate();
                                            if (i > 0) {
                                                System.out.println("Successfully added to your playlist");
                                            }
                                        }
                                    }
                                    break;
                                case 2:
                                    System.out.println("Please select :\n1.Search by Celebrity Name,\n2.Search by Podcast released date:");
                                    int opt7 = scan.nextInt();
                                    ResultSet rs3 = null;
                                    switch (opt7) {
                                        case 1:
                                            System.out.println("Please enter Celebrity Name:");
                                            scan.nextLine();
                                            String ans = scan.nextLine();
                                            rs3 = st.executeQuery("select * from podcast where podcaster_name='" + ans + "'");
                                            break;
                                        case 2:
                                            System.out.println("Please enter Podcast released date in (YYYY/MM/DD) format:");
                                            scan.nextLine();
                                            Date ans1 = Date.valueOf(scan.next());
                                            rs3 = st.executeQuery("select * from podcast where releasedate='"+ans1+"'");
                                            break;
                                    }
                                    System.out.println("Search Results:");
                                    System.out.format("%10s %15s %10s %15s %10s", "Podcast Id", "Podcast Name", "Language", "Release date", "Celebrity Name");
                                    System.out.println("");
                                    int j=0;
                                    while (rs3.next()) {
                                        System.out.format("%10d %15s %10s %15s %10s", rs3.getInt(1), rs3.getString(2), rs3.getString(3), rs3.getString(4), rs3.getString(5));
                                        System.out.println("");
                                        j++;
                                    }
                                    if(j==0)
                                    {
                                        System.out.println("No search results");
                                        break;
                                    }
                                    System.out.println("Please select Podcast Id");
                                    opt = scan.nextInt();
                                    System.out.format("%10s %15s %10s %15s %10s", "Podcast Id", "Episode Id", "Episode Name", "On air date", "Celebrity Name");
                                    System.out.println("");
                                    ResultSet rs16 = st.executeQuery("select * from podcast join podcast_episodes where podcast.podcast_id=podcast_episodes.podcast_id and podcast.podcast_id=" + opt);
                                    while (rs16.next()) {
                                        System.out.format("%10d %15d %10s %15s %10s", rs16.getInt(1), rs16.getInt(6), rs16.getString(8), rs16.getString(9), rs16.getString(5));
                                        System.out.println("");
                                    }
                                    System.out.println("Please select episode Id:");
                                    opt2 = scan.nextInt();
                                    ResultSet rs10 = st.executeQuery("select filepath from podcast_episodes where episode_id=" + opt2);
                                    while (rs10.next()) {
                                        SimpleAudioPlayer.play(rs10.getString(1));
                                    }
                                    break;
                                }
                            }while (opt10!=3);
                        break;

                    case 3:ResultSet rs5=st.executeQuery("select * from playlist join user where user.userid=playlist.userid and user.userid='"+user_id+"'");
                        System.out.format("%10s %10s","Playlist Id","Playlist Name");
                        System.out.println("");
                        int i=0;
                        while (rs5.next())
                        {
                            System.out.format("%10d %10s",rs5.getInt(1),rs5.getString(2));
                            System.out.println("");
                            i++;
                        }
                        if(i==0)
                        {
                            System.out.println("No playlists available");
                            break;
                        }
                        System.out.println("Enter Playlist Id");
                        int choice3= scan.nextInt();
                        System.out.println("Select your choice \n1.Songs,\n2.Podcasts");
                        int choice6= scan.nextInt();
                        if(choice6==1) {
                            ResultSet rs6=st.executeQuery("select * from user join playlist join playlist_song_details join song where user.userid=playlist.userid and playlist.playlist_id=playlist_song_details.playlist_id and song.song_id=playlist_song_details.song_id and user.userid='"+user_id+"' and playlist.playlist_id="+choice3);
                            System.out.println("****Songs in your Playlist****");
                            System.out.format("%10s %15s", "Song Id", "Song Name");
                            System.out.println("");
                            while (rs6.next()) {
                                System.out.format("%10d %10s", rs6.getInt(12), rs6.getString(13));
                                System.out.println("");
                            }
                            System.out.println("Select Song ID");
                            int choice4 = scan.nextInt();
                            ResultSet rs7 = st.executeQuery("select * from user join playlist join playlist_song_details join song where user.userid=playlist.userid and playlist.playlist_id=playlist_song_details.playlist_id and song.song_id=playlist_song_details.song_id and playlist.playlist_id=" + choice3 + " and song.song_id=" + choice4);
                            while (rs7.next()) {
                                SimpleAudioPlayer.play(rs7.getString(18));
                            }
                        }
                        if(choice6==2)
                        {
                            ResultSet rs6=st.executeQuery("select * from user join playlist join playlist_podcast_details join podcast where user.userid=playlist.userid and playlist.playlist_id=playlist_podcast_details.playlist_id and user.userid='"+user_id+"' and playlist.playlist_id="+choice3);
                            System.out.println("****Podcasts in your Playlist****");
                            System.out.format("%10s %15s", "Podcast Id", "Podcast Name");
                            System.out.println("");
                            int l=0;
                            while (rs6.next()) {
                                System.out.format("%10d %10s", rs6.getInt(12), rs6.getString(13));
                                System.out.println("");
                                l++;
                            }
                            if(l==0)
                            {
                                System.out.println("No podcasts available");
                                break;
                            }
                            System.out.println("Select Podcast ID");
                            int choice4 = scan.nextInt();
                            ResultSet rs1 = st.executeQuery("select * from podcast join podcast_episodes where podcast.podcast_id=podcast_episodes.podcast_id and podcast.podcast_id=" + choice4);
                            System.out.format("%10s %15s %10s %15s %10s", "Podcast Id", "Episode Id", "Episode Name", "On air date", "Celebrity Name");
                            System.out.println("");
                            while (rs1.next()) {
                                System.out.format("%10d %15d %10s %15s %10s", rs1.getInt(1), rs1.getInt(6), rs1.getString(8), rs1.getString(9), rs1.getString(5));
                                System.out.println("");
                            }
                            System.out.println("Please select episode Id:");
                            int opt2 = scan.nextInt();
                            ResultSet rs9 = st.executeQuery("select filepath from podcast_episodes where episode_id=" + opt2);
                            while (rs9.next()) {
                                SimpleAudioPlayer.play(rs9.getString(1));
                            }

                        }
                        break;
                    case 4:
                        System.out.println("------------------------Thank you---------------------------");

                }
            }while(choice != 4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
