package com.itismob.s03.groupiseight.adapted.data;

import com.itismob.s03.groupiseight.adapted.R;
import com.itismob.s03.groupiseight.adapted.models.CommentModel;
import com.itismob.s03.groupiseight.adapted.models.MediaCategory;
import com.itismob.s03.groupiseight.adapted.models.MediaItem;
import com.itismob.s03.groupiseight.adapted.models.ReviewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class SampleMediaData {

    private SampleMediaData() {
    }

    private static final MediaItem BOOK_HUNGER_GAMES = new MediaItem(
            1, "The Hunger Games", "Suzanne Collins",
            MediaCategory.BOOK, 2008, 4.3f,
            "Scholastic", null,
            "Adapted into the 2012 film", 13,
            R.drawable.cover_book_hunger_games);
    private static final MediaItem BOOK_DUNE = new MediaItem(
            2, "Dune", "Frank Herbert",
            MediaCategory.BOOK, 1965, 4.4f,
            "Chilton Books", null,
            "Adapted into the 2021 film", 14,
            R.drawable.cover_book_dune);
    private static final MediaItem BOOK_GONE_GIRL = new MediaItem(
            3, "Gone Girl", "Gillian Flynn",
            MediaCategory.BOOK, 2012, 4.0f,
            "Crown Publishing", null,
            "Adapted into the 2014 film", 15,
            R.drawable.cover_book_gone_girl);
    private static final MediaItem BOOK_PRIDE_AND_PREJUDICE = new MediaItem(
            4, "Pride and Prejudice", "Jane Austen",
            MediaCategory.BOOK, 1813, 4.3f,
            "T. Egerton", null,
            "Adapted into the 2005 film", 16,
            R.drawable.cover_book_pride_and_prejudice);
    private static final MediaItem BOOK_HARRY_POTTER = new MediaItem(
            5, "Harry Potter and the Sorcerer's Stone", "J.K. Rowling",
            MediaCategory.BOOK, 1998, 4.5f,
            "Scholastic", null,
            "Adapted into the 2001 film", 17,
            R.drawable.cover_book_harry_potter);
    private static final MediaItem BOOK_LITTLE_WOMEN = new MediaItem(
            6, "Little Women", "Louisa May Alcott",
            MediaCategory.BOOK, 1868, 4.2f,
            "Roberts Brothers", null,
            "Adapted into the 2019 film", 18,
            R.drawable.cover_book_little_women);
    private static final MediaItem BOOK_CRAZY_RICH_ASIANS = new MediaItem(
            7, "Crazy Rich Asians", "Kevin Kwan",
            MediaCategory.BOOK, 2013, 3.8f,
            "Doubleday", null,
            "Adapted into the 2018 film", 19,
            R.drawable.cover_book_crazy_rich_asians);
    private static final MediaItem BOOK_GAME_OF_THRONES = new MediaItem(
            8, "A Game of Thrones", "George R. R. Martin",
            MediaCategory.BOOK, 1996, 4.5f,
            "Bantam Spectra", null,
            "Adapted into the TV series Game of Thrones", 22,
            R.drawable.cover_book_game_of_thrones);
    private static final MediaItem BOOK_NORMAL_PEOPLE = new MediaItem(
            9, "Normal People", "Sally Rooney",
            MediaCategory.BOOK, 2018, 3.9f,
            "Faber & Faber", null,
            "Adapted into the 2020 TV series", 23,
            R.drawable.cover_book_normal_people);
    private static final MediaItem BOOK_THE_LAST_WISH = new MediaItem(
            10, "The Last Wish", "Andrzej Sapkowski",
            MediaCategory.BOOK, 2007, 4.2f,
            "Orbit", null,
            "Inspired the TV series The Witcher", 24,
            R.drawable.cover_book_the_last_wish);
    private static final MediaItem BOOK_THE_DUKE_AND_I = new MediaItem(
            11, "The Duke and I", "Julia Quinn",
            MediaCategory.BOOK, 2000, 4.0f,
            "Avon", null,
            "Adapted into the TV series Bridgerton", 25,
            R.drawable.cover_book_the_duke_and_i);
    private static final MediaItem BOOK_MIDNIGHT_LIBRARY = new MediaItem(
            12, "The Midnight Library", "Matt Haig",
            MediaCategory.BOOK, 2020, 4.0f,
            "Canongate", null,
            null, null,
            R.drawable.cover_book_midnight_library);
    private static final MediaItem MOVIE_HUNGER_GAMES = new MediaItem(
            13, "The Hunger Games", "Gary Ross",
            MediaCategory.MOVIE, 2012, 4.0f,
            null, null,
            "Based on the book by Suzanne Collins", 1,
            R.drawable.cover_movie_hunger_games);
    private static final MediaItem MOVIE_DUNE = new MediaItem(
            14, "Dune", "Denis Villeneuve",
            MediaCategory.MOVIE, 2021, 4.3f,
            null, null,
            "Based on the book by Frank Herbert", 2,
            R.drawable.cover_movie_dune);
    private static final MediaItem MOVIE_GONE_GIRL = new MediaItem(
            15, "Gone Girl", "David Fincher",
            MediaCategory.MOVIE, 2014, 4.1f,
            null, null,
            "Based on the book by Gillian Flynn", 3,
            R.drawable.cover_movie_gone_girl);
    private static final MediaItem MOVIE_PRIDE_AND_PREJUDICE = new MediaItem(
            16, "Pride & Prejudice", "Joe Wright",
            MediaCategory.MOVIE, 2005, 4.0f,
            null, null,
            "Based on the book by Jane Austen", 4,
            R.drawable.cover_movie_pride_and_prejudice);
    private static final MediaItem MOVIE_HARRY_POTTER = new MediaItem(
            17, "Harry Potter and the Sorcerer's Stone", "Chris Columbus",
            MediaCategory.MOVIE, 2001, 4.1f,
            null, null,
            "Based on the book by J.K. Rowling", 5,
            R.drawable.cover_movie_harry_potter);
    private static final MediaItem MOVIE_LITTLE_WOMEN = new MediaItem(
            18, "Little Women", "Greta Gerwig",
            MediaCategory.MOVIE, 2019, 4.2f,
            null, null,
            "Based on the book by Louisa May Alcott", 6,
            R.drawable.cover_movie_little_women);
    private static final MediaItem MOVIE_CRAZY_RICH_ASIANS = new MediaItem(
            19, "Crazy Rich Asians", "Jon M. Chu",
            MediaCategory.MOVIE, 2018, 3.8f,
            null, null,
            "Based on the book by Kevin Kwan", 7,
            R.drawable.cover_movie_crazy_rich_asians);
    private static final MediaItem MOVIE_PARASITE = new MediaItem(
            20, "Parasite", "Bong Joon-ho",
            MediaCategory.MOVIE, 2019, 4.6f,
            null, null,
            null, null,
            R.drawable.cover_movie_parasite);
    private static final MediaItem MOVIE_INCEPTION = new MediaItem(
            21, "Inception", "Christopher Nolan",
            MediaCategory.MOVIE, 2010, 4.5f,
            null, null,
            null, null,
            R.drawable.cover_movie_inception);
    private static final MediaItem TV_GAME_OF_THRONES = new MediaItem(
            22, "Game of Thrones", "D. B. Weiss & David Benioff",
            MediaCategory.TV_SERIES, 2011, 4.4f,
            null, null,
            "Based on the books by George R. R. Martin", 8,
            R.drawable.cover_tv_game_of_thrones);
    private static final MediaItem TV_NORMAL_PEOPLE = new MediaItem(
            23, "Normal People", "Lenny Abrahamson & Hettie Macdonald",
            MediaCategory.TV_SERIES, 2020, 4.1f,
            null, null,
            "Based on the book by Sally Rooney", 9,
            R.drawable.cover_tv_normal_people);
    private static final MediaItem TV_THE_WITCHER = new MediaItem(
            24, "The Witcher", "Lauren Schmidt Hissrich",
            MediaCategory.TV_SERIES, 2019, 3.9f,
            null, null,
            "Based on the books by Andrzej Sapkowski", 10,
            R.drawable.cover_tv_the_witcher);
    private static final MediaItem TV_BRIDGERTON = new MediaItem(
            25, "Bridgerton", "Chris Van Dusen",
            MediaCategory.TV_SERIES, 2020, 3.8f,
            null, null,
            "Based on the books by Julia Quinn", 11,
            R.drawable.cover_tv_bridgerton);
    private static final MediaItem TV_BREAKING_BAD = new MediaItem(
            26, "Breaking Bad", "Vince Gilligan",
            MediaCategory.TV_SERIES, 2008, 4.8f,
            null, null,
            null, null,
            R.drawable.cover_tv_breaking_bad);
    private static final MediaItem TV_STRANGER_THINGS = new MediaItem(
            27, "Stranger Things", "The Duffer Brothers",
            MediaCategory.TV_SERIES, 2016, 4.2f,
            null, null,
            null, null,
            R.drawable.cover_tv_stranger_things);
    private static final MediaItem MUSIC_BLINDING_LIGHTS = new MediaItem(
            28, "Blinding Lights", "The Weeknd",
            MediaCategory.MUSIC, 2020, 4.6f,
            null, "After Hours",
            null, null,
            R.drawable.cover_music_blinding_lights);
    private static final MediaItem MUSIC_FLOWERS = new MediaItem(
            29, "Flowers", "Miley Cyrus",
            MediaCategory.MUSIC, 2023, 4.2f,
            null, "Endless Summer Vacation",
            null, null,
            R.drawable.cover_music_flowers);
    private static final MediaItem MUSIC_ANTI_HERO = new MediaItem(
            30, "Anti-Hero", "Taylor Swift",
            MediaCategory.MUSIC, 2022, 4.3f,
            null, "Midnights",
            null, null,
            R.drawable.cover_music_anti_hero);
    private static final MediaItem MUSIC_BOHEMIAN_RHAPSODY = new MediaItem(
            31, "Bohemian Rhapsody", "Queen",
            MediaCategory.MUSIC, 1975, 4.9f,
            null, "A Night at the Opera",
            "Featured in the 2018 film Bohemian Rhapsody", null,
            R.drawable.cover_music_bohemian_rhapsody);
    private static final MediaItem MUSIC_LEVITATING = new MediaItem(
            32, "Levitating", "Dua Lipa",
            MediaCategory.MUSIC, 2020, 4.2f,
            null, "Future Nostalgia",
            null, null,
            R.drawable.cover_music_levitating);
    private static final MediaItem MUSIC_AS_IT_WAS = new MediaItem(
            33, "As It Was", "Harry Styles",
            MediaCategory.MUSIC, 2022, 4.1f,
            null, "Harry's House",
            null, null,
            R.drawable.cover_music_as_it_was);
    private static final MediaItem MUSIC_SAFE_AND_SOUND = new MediaItem(
            34, "Safe & Sound", "Taylor Swift ft. The Civil Wars",
            MediaCategory.MUSIC, 2012, 4.0f,
            null, "The Hunger Games: Songs from District 12 and Beyond",
            "From the soundtrack of The Hunger Games film", 13,
            R.drawable.cover_music_safe_and_sound);
    private static final MediaItem MUSIC_DUNE_SOUNDTRACK = new MediaItem(
            35, "Dune (Original Motion Picture Soundtrack)", "Hans Zimmer",
            MediaCategory.MUSIC, 2021, 4.5f,
            null, "Dune (Original Motion Picture Soundtrack)",
            "Score for the 2021 film Dune", 14,
            R.drawable.cover_music_dune_soundtrack);
    private static final MediaItem MUSIC_HOTEL_CALIFORNIA = new MediaItem(
            36, "Hotel California", "Eagles",
            MediaCategory.MUSIC, 1976, 4.7f,
            null, "Hotel California",
            null, null,
            R.drawable.cover_music_hotel_california);

    public static List<MediaItem> getAllMedia() {
        return Collections.unmodifiableList(Arrays.asList(
                BOOK_HUNGER_GAMES, BOOK_DUNE, BOOK_GONE_GIRL, BOOK_PRIDE_AND_PREJUDICE,
                BOOK_HARRY_POTTER, BOOK_LITTLE_WOMEN, BOOK_CRAZY_RICH_ASIANS, BOOK_GAME_OF_THRONES,
                BOOK_NORMAL_PEOPLE, BOOK_THE_LAST_WISH, BOOK_THE_DUKE_AND_I, BOOK_MIDNIGHT_LIBRARY,
                MOVIE_HUNGER_GAMES, MOVIE_DUNE, MOVIE_GONE_GIRL, MOVIE_PRIDE_AND_PREJUDICE,
                MOVIE_HARRY_POTTER, MOVIE_LITTLE_WOMEN, MOVIE_CRAZY_RICH_ASIANS, MOVIE_PARASITE,
                MOVIE_INCEPTION, TV_GAME_OF_THRONES, TV_NORMAL_PEOPLE, TV_THE_WITCHER,
                TV_BRIDGERTON, TV_BREAKING_BAD, TV_STRANGER_THINGS, MUSIC_BLINDING_LIGHTS,
                MUSIC_FLOWERS, MUSIC_ANTI_HERO, MUSIC_BOHEMIAN_RHAPSODY, MUSIC_LEVITATING,
                MUSIC_AS_IT_WAS, MUSIC_SAFE_AND_SOUND, MUSIC_DUNE_SOUNDTRACK, MUSIC_HOTEL_CALIFORNIA));
    }

    public static MediaItem getMediaById(int id) {
        for (MediaItem item : getAllMedia()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public static List<MediaItem> getTrendingMedia() {
        return Collections.unmodifiableList(Arrays.asList(
                MOVIE_PARASITE, MUSIC_BLINDING_LIGHTS, TV_STRANGER_THINGS, TV_BREAKING_BAD,
                MOVIE_INCEPTION, MUSIC_ANTI_HERO, BOOK_HARRY_POTTER, MUSIC_FLOWERS));
    }

    public static List<MediaItem> getTrendingAdaptations() {
        return Collections.unmodifiableList(Arrays.asList(
                MOVIE_DUNE, TV_GAME_OF_THRONES, MOVIE_HUNGER_GAMES, TV_BRIDGERTON,
                TV_THE_WITCHER, MOVIE_LITTLE_WOMEN, TV_NORMAL_PEOPLE, MOVIE_GONE_GIRL));
    }

    public static List<MediaItem> getRecentlyAdded() {
        return Collections.unmodifiableList(Arrays.asList(
                BOOK_MIDNIGHT_LIBRARY, MUSIC_DUNE_SOUNDTRACK, MOVIE_CRAZY_RICH_ASIANS,
                MUSIC_AS_IT_WAS, BOOK_THE_DUKE_AND_I, BOOK_NORMAL_PEOPLE, MUSIC_SAFE_AND_SOUND));
    }

    public static List<MediaItem> getRecommendedMedia() {
        return Collections.unmodifiableList(Arrays.asList(
                BOOK_DUNE, BOOK_GAME_OF_THRONES, MOVIE_PRIDE_AND_PREJUDICE, MUSIC_BOHEMIAN_RHAPSODY,
                BOOK_THE_LAST_WISH, BOOK_GONE_GIRL, MUSIC_HOTEL_CALIFORNIA, BOOK_LITTLE_WOMEN));
    }

    // --- Module H: Sample Community Data ---

    private static final List<ReviewModel> SAMPLE_REVIEWS = new ArrayList<>(Arrays.asList(
            new ReviewModel(101, 2, "Elena Rostova", 5.0f, "Denis Villeneuve captured the grandeur of Herbert's universe perfectly! The score by Hans Zimmer elevates every single scene.", "Oct 24, 2023", 42, 3, false),
            new ReviewModel(102, 2, "Marcus Vance", 4.0f, "A cinematic masterpiece, though pacing slows down in the middle section. Excellent world building nonetheless.", "Nov 02, 2023", 18, 1, false),
            new ReviewModel(103, 1, "Sophia Chen", 4.5f, "An intense, faithful adaptation of Collins' novel. Jennifer Lawrence was phenomenal as Katniss Everdeen.", "Jan 12, 2024", 29, 2, false),
            new ReviewModel(104, 25, "Clara Oswald", 4.0f, "Addictive costume drama! Gorgeous aesthetics and fantastic string quartet pop covers.", "Dec 18, 2023", 15, 0, false),
            new ReviewModel(105, 20, "David Miller", 5.0f, "An unbelievable piece of modern cinema. Masterclass in suspense and social commentary.", "Feb 05, 2024", 56, 4, false)
    ));

    private static final List<CommentModel> SAMPLE_COMMENTS = new ArrayList<>(Arrays.asList(
            new CommentModel(201, 101, "Liam Thorne", "Totally agree about Zimmer's soundtrack! The sardaukar chant still gives me chills.", "2 hours ago"),
            new CommentModel(202, 101, "Hannah Abbott", "The visuals in IMAX were unreal. Can't wait for Part Two!", "1 day ago"),
            new CommentModel(203, 101, "Alex Mercer", "Do you think it adapted the book better than the 1984 film?", "2 days ago"),
            new CommentModel(204, 102, "Chloe Bennet", "Fair point about the pacing, but the world building makes up for it.", "3 days ago"),
            new CommentModel(205, 103, "Ryan Gosling Fan", "The arena scenes were shot so well. Catching Fire was even better!", "4 days ago")
    ));

    public static List<ReviewModel> getReviewsForMedia(int mediaId) {
        List<ReviewModel> filtered = new ArrayList<>();
        for (ReviewModel review : SAMPLE_REVIEWS) {
            if (review.getMediaId() == mediaId) {
                filtered.add(review);
            }
        }
        return filtered;
    }

    public static ReviewModel getReviewById(int reviewId) {
        for (ReviewModel review : SAMPLE_REVIEWS) {
            if (review.getId() == reviewId) {
                return review;
            }
        }
        return null;
    }

    public static List<CommentModel> getCommentsForReview(int reviewId) {
        List<CommentModel> filtered = new ArrayList<>();
        for (CommentModel comment : SAMPLE_COMMENTS) {
            if (comment.getReviewId() == reviewId) {
                filtered.add(comment);
            }
        }
        return filtered;
    }

    public static void toggleReviewLike(int reviewId) {
        ReviewModel review = getReviewById(reviewId);
        if (review != null) {
            review.toggleLike();
        }
    }

    public static CommentModel addComment(int reviewId, String authorName, String commentText) {
        int newId = 200 + SAMPLE_COMMENTS.size() + 1;
        CommentModel newComment = new CommentModel(newId, reviewId, authorName, commentText, "Just now");
        SAMPLE_COMMENTS.add(newComment);

        ReviewModel review = getReviewById(reviewId);
        if (review != null) {
            review.incrementCommentCount();
        }
        return newComment;
    }

    public static ReviewModel addReview(int mediaId, String authorName, float rating, String reviewText) {
        int newId = 100 + SAMPLE_REVIEWS.size() + 1;
        ReviewModel newReview = new ReviewModel(newId, mediaId, authorName, rating, reviewText, "Just now", 0, 0, false);
        SAMPLE_REVIEWS.add(newReview);
        return newReview;
    }
}