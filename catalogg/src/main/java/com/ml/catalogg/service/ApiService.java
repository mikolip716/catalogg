package com.ml.catalogg.service;

import com.ml.catalogg.entity.Album;
import com.ml.catalogg.entity.Artist;
import com.ml.catalogg.entity.Genre;
import com.ml.catalogg.entity.Songs;
import com.ml.catalogg.helper.FormatEnumParser;
import com.ml.catalogg.helper.TableMediaLink;
import com.ml.catalogg.helper.TypeEnumParser;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {
    @Getter
    private TableMediaLink musicbrainzLink;
    @Getter
    private boolean hasCover;
    private final String titleXPath = "/metadata/release/title";
    private final String artistXPath = "/metadata/release/artist-credit/name-credit/artist/name";
    private final String genreXPath = "/metadata/release/release-group/genre-list/genre";
    private final String typeXPath = "/metadata/release/release-group/primary-type";
    private final String secondaryTypeXPath = "/metadata/release/release-group/secondary-type-list/secondary-type";
    private final String relDateXPath = "/metadata/release/date";
    private final String countryXPath = "/metadata/release/country";
    private final String hasCoverXPath = "/metadata/release/cover-art-archive/front";
    private final String formatXPath = "/metadata/release/medium-list/medium/format";
    private final String songNumberXPath = "/metadata/release/medium-list/medium/track-list/track/number";
    private final String songTitleXPath = "/metadata/release/medium-list/medium/track-list/track/recording/title";
    private final String songLengthXPath = "/metadata/release/medium-list/medium/track-list/track/length";
    private final String userAgent = "CatalOgg/1.0-RELEASE (mikolip716@student.polsl.pl)";
    public String getCover (String mbid) throws IOException {
        String baseCoverUrlStart = "https://coverartarchive.org/release/";
        String baseCoverUrlEnd = "/front-250";
        String fullCoverUrl = baseCoverUrlStart + mbid + baseCoverUrlEnd;
        String coverFilePath = null;
        URL coverUrl = new URL(fullCoverUrl);
        try {
            Files.createDirectories(Paths.get(System.getProperty("user.dir") + "\\covers")); //create dir if it doesn't exist
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File fileOutput = new File(System.getProperty("user.dir") + "\\covers\\" + mbid + ".jpg");
        BufferedImage coverDownloaded = ImageIO.read(coverUrl);
        ImageIO.write(coverDownloaded, "jpg", fileOutput);
        coverFilePath = fileOutput.getAbsolutePath();
        return coverFilePath;
    }
    private Document sendRequest (String fullUrl)
            throws Exception {
        HttpRequest request = null;
        request = HttpRequest.newBuilder(new URI(fullUrl))
                .GET()
                .headers("UserAgent", userAgent)
                .timeout(Duration.ofSeconds(10))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        return docBuilder.parse(new InputSource(new StringReader(response.body())));
    }
    public String getMbid(String fullUrl) throws Exception {
        Document doc = sendRequest(fullUrl);
        NodeList releaseList = doc.getElementsByTagName("release");
        if (releaseList != null) {
            Node release = releaseList.item(0);
            return release.getAttributes().getNamedItem("id").getNodeValue();
        } else {
            return null;
        }
    }
    public Album getAlbum(String fullUrl) throws Exception {
        Album apiAlbum = new Album();
        Document doc = sendRequest(fullUrl);
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node titleNode = (Node) xPath.compile(titleXPath).evaluate(doc, XPathConstants.NODE);
        Node typeNode = (Node) xPath.compile(typeXPath).evaluate(doc, XPathConstants.NODE);
        Node relDateNode = (Node) xPath.compile(relDateXPath).evaluate(doc, XPathConstants.NODE);
        Node countryNode = (Node) xPath.compile(countryXPath).evaluate(doc, XPathConstants.NODE);
        Node hasCoverNode = (Node) xPath.compile(hasCoverXPath).evaluate(doc, XPathConstants.NODE);
        Node formatNode = (Node) xPath.compile(formatXPath).evaluate(doc, XPathConstants.NODE);
        NodeList songNumberNodeList = (NodeList) xPath.compile(songNumberXPath).evaluate(doc, XPathConstants.NODESET);
        NodeList songTitleNodeList = (NodeList) xPath.compile(songTitleXPath).evaluate(doc, XPathConstants.NODESET);
        NodeList songLengthNodeList = (NodeList) xPath.compile(songLengthXPath).evaluate(doc, XPathConstants.NODESET);
        NodeList artistsNodeList = (NodeList) xPath.compile(artistXPath).evaluate(doc, XPathConstants.NODESET);
        NodeList genreNodeList = (NodeList) xPath.compile(genreXPath).evaluate(doc, XPathConstants.NODESET);
        NodeList secondaryTypeNodeList = (NodeList) xPath.compile(secondaryTypeXPath).evaluate(doc, XPathConstants.NODESET);

        NodeList releaseList = doc.getElementsByTagName("release");
        Node release = releaseList.item(0);
        if (release == null) {
            throw new NullPointerException("release is empty");
        }
        String releaseMbid = release.getAttributes().getNamedItem("id").getNodeValue();
        hasCover = hasCoverNode.getTextContent().equals("true");
        musicbrainzLink = new TableMediaLink("MusicBrainz", "https://musicbrainz.org/release/" + releaseMbid);

        if (titleNode != null) {
            apiAlbum.setTitle(titleNode.getTextContent());
        }
        if (countryNode != null) {
            apiAlbum.setCountry(countryNode.getTextContent());
        }
        if (formatNode != null) {
            apiAlbum.setFormat(FormatEnumParser.parse(formatNode.getTextContent()).toString());
        }
        if (secondaryTypeNodeList.getLength() > 0) {
            Node secondaryTypeNode = secondaryTypeNodeList.item(secondaryTypeNodeList.getLength() - 1);
            apiAlbum.setType(TypeEnumParser.parse(secondaryTypeNode.getTextContent()).toString());
        } else {
            apiAlbum.setType(TypeEnumParser.parse(typeNode.getTextContent()).toString());
        }

        if (relDateNode.getTextContent().length() > 7) {    //full date
            apiAlbum.setReleaseDate(LocalDate.parse(relDateNode.getTextContent()));
        } else if (relDateNode.getTextContent().length() == 7) {  //if the date provided is year and month only, set as 1st of the month
            int year = Integer.parseInt(relDateNode.getTextContent().substring(0, 4));
            int month = Integer.parseInt(relDateNode.getTextContent().substring(5, 7));
            LocalDate releaseDate = LocalDate.of(year, month, 1);
            apiAlbum.setReleaseDate(releaseDate);
        } else if (relDateNode.getTextContent().length() == 4) {  //if the date provided is year only, set as January 1st of that year
            LocalDate releaseDate = LocalDate.of(Integer.parseInt(relDateNode.getTextContent()), 1, 1);
            apiAlbum.setReleaseDate(releaseDate);
        }

        List<Artist> newArtistList = new ArrayList<>();
        if (artistsNodeList.getLength() > 0) {
            for (int i = 0; i < artistsNodeList.getLength(); i++) {
                Node currentNode = artistsNodeList.item(i);
                Artist tempArtist = new Artist();
                tempArtist.setName(currentNode.getTextContent());
                newArtistList.add(tempArtist);
            }
        }
        apiAlbum.setArtists(newArtistList);

        List<Genre> newGenreList = new ArrayList<>();
        if (genreNodeList.getLength() > 0) {
            for (int i = 0; i < genreNodeList.getLength(); i++) {
                Node currentNode = genreNodeList.item(i);
                Genre tempGenre = new Genre();
                tempGenre.setName(currentNode.getTextContent());
                newGenreList.add(tempGenre);
            }
        }
        apiAlbum.setGenres(newGenreList);

        List<Songs> newSongsList = new ArrayList<>();
        if (songNumberNodeList.getLength() > 0) {
            for (int i = 0; i < songNumberNodeList.getLength(); i++) {
                Node currentSongTitle = songTitleNodeList.item(i);
                Node currentSongNumber = songNumberNodeList.item(i);
                Node currentSongLength = songLengthNodeList.item(i);
                Songs tempSong = new Songs();
                if (currentSongNumber != null) {
                    tempSong.setNumber(currentSongNumber.getTextContent());
                } else {
                    tempSong.setNumber("?");
                }
                if (currentSongTitle != null) {
                    tempSong.setTitle(currentSongTitle.getTextContent());
                } else {
                    tempSong.setTitle("No title provided");
                }
                if (currentSongLength != null) {
                    tempSong.setLength(Integer.parseInt(currentSongLength.getTextContent())/1000);
                } else {
                    tempSong.setLength(0);
                }
                newSongsList.add(tempSong);
            }
        }
        apiAlbum.setSongs(newSongsList);
        return apiAlbum;
    }

}
