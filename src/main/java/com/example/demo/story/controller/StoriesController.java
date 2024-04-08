package com.example.demo.story.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.demo.story.Topstories.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@RestController
public class StoriesController {

    @GetMapping("/getTimeStories")
    public List<Story> getLatestTimeStories() {
        List<Story> stories = new ArrayList<>();
        String timeUrl = "https://time.com";//set Api Url and key

        try {
            RestTemplate restTemplate = new RestTemplate();
            String htmlContent = restTemplate.getForObject(timeUrl, String.class);
            stories = extractLatestStories(htmlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stories;
    }
    private List<Story> extractLatestStories(String htmlContent) {
        List<Story> stories = new ArrayList<>();
        String startMarker = "<h3 class=\"headline\">"; // Updated marker for story titles
        String endMarker = "</h3>";
        String linkStartMarker = "<a href=\"";
        String linkEndMarker = "\"";

        Pattern pattern = Pattern.compile(Pattern.quote(startMarker) + "(.*?)" + Pattern.quote(endMarker));
        Matcher matcher = pattern.matcher(htmlContent);

        while (matcher.find() && stories.size() < 6) {
            String storyContent = matcher.group(1);
            String title = storyContent.trim();

            int linkStartIndex = storyContent.indexOf(linkStartMarker) + linkStartMarker.length();
            int linkEndIndex = storyContent.indexOf(linkEndMarker, linkStartIndex);

            if (linkStartIndex != -1 && linkEndIndex != -1) {
                String link = storyContent.substring(linkStartIndex, linkEndIndex);
                stories.add(new Story(title, "https://time.com" + link));
            }
        }

        
        return stories;
    }

   
   
        }