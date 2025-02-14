package com.example.vibescan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.vibescan.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.container.visibility = View.INVISIBLE
        binding.shimmer.startShimmer()

        val sentimentQuestions = arrayListOf(
            "1. What are the first three words that come to mind when you think about this person?",
            "2. How do you feel when you see their name pop up on your phone?",
            "3. If you had to describe their personality in one sentence, what would it be?",
            "4. What song or movie reminds you of them and why?",
            "5. How would you describe your last conversation with them?",
            "6. What is one thing they do that always makes you smile?",
            "7. If you had to assign them a color, what would it be and why?",
            "8. What is a memory with them that stands out the most?",
            "9. How would you feel if you didn't see or talk to them for a month?",
            "10. What advice would you give them if they asked you about their life choices?",
            "11. If you could change one thing about your relationship with them, what would it be?",
            "12. How would you feel if they completely disappeared from your life tomorrow?",
            "13. What do you think they value the most in life?",
            "14. If they were a fictional character, who would they be and why?"
        )

        val sentimentMetrics = arrayListOf(
            "Initial Perception",
            "Emotional Response",
            "Personality Assessment",
            "Association & Connection",
            "Communication Quality",
            "Happiness Triggers",
            "Symbolic Representation",
            "Memorable Moments",
            "Emotional Dependency",
            "Guidance & Perspective",
            "Relationship Evaluation",
            "Impact of Absence",
            "Core Values Insight",
            "Character Archetype"
        )

        val adapter = CustomAdapter(this, this, sentimentQuestions, sentimentMetrics);
        binding.listView.adapter = adapter

        binding.shimmer.postDelayed({
            binding.shimmer.stopShimmer()
            binding.container.visibility = View.VISIBLE
            binding.shimmer.visibility = View.GONE
        }, 2000)

        val submitButton = findViewById<Button>(R.id.getResultButton)
        submitButton.setOnClickListener {
            val resultIntent = Intent(this, PageDesign::class.java).apply {
            }
            startActivity(resultIntent)
        }
    }

    fun analyzeSentence(sentence: String) {

        val dbHelper = DB(this)

        CoroutineScope(Dispatchers.IO).launch {
            var positiveCount = 0;
            var negativeCount = 0;
            val cleanText = sentence.replace("[^a-zA-Z\\s]","")
            val arrayOfWords : List<String> = cleanText.trim().split(" ")
            val positiveWord = listOf(
                "amazing", "awesome", "beautiful", "brilliant", "cheerful", "compassionate", "confident",
                "courageous", "creative", "delightful", "efficient", "elegant", "encouraging", "energetic",
                "excellent", "exceptional", "fabulous", "fantastic", "friendly", "generous", "gentle",
                "genuine", "graceful", "grateful", "great", "happy", "harmonious", "helpful", "honest",
                "hopeful", "incredible", "inspiring", "intelligent", "joyful", "kind", "knowledgeable",
                "lively", "lovely", "loyal", "magnificent", "marvelous", "motivated", "nice", "optimistic",
                "outstanding", "passionate", "peaceful", "perfect", "pleasant", "positive", "powerful",
                "proactive", "productive", "radiant", "reliable", "resilient", "respectful", "responsible",
                "satisfying", "selfless", "sincere", "smart", "spectacular", "splendid", "successful",
                "supportive", "terrific", "thankful", "thoughtful", "trustworthy", "understanding",
                "unique", "upbeat", "vibrant", "victorious", "warmhearted", "welcoming", "wholehearted",
                "wise", "wonderful", "worthy", "zealous", "ambitious", "approachable", "attentive",
                "charismatic", "cheerful", "charming", "considerate", "determined", "enthusiastic",
                "fearless", "forgiving", "hardworking", "humble", "inventive", "joyous", "lovable",
                "mindful", "respectable", "spirited", "tender"
            )

            val negativeWord = listOf(
                "angry", "anxious", "annoyed", "appalling", "awful", "bad", "bitter", "boring", "chaotic",
                "clumsy", "cold", "combative", "confused", "corrupt", "cruel", "damaging", "dark", "deceitful",
                "defeated", "depressed", "desperate", "difficult", "disappointing", "disastrous",
                "dishonest", "distressing", "dreadful", "embarrassed", "envious", "evil", "failed",
                "fearful", "filthy", "frustrated", "gloomy", "greedy", "grim", "guilty", "hateful", "heartless",
                "helpless", "hopeless", "hostile", "hurtful", "ignorant", "illogical", "immoral",
                "impatient", "impolite", "incompetent", "inconsiderate", "insecure", "insensitive",
                "intolerant", "jealous", "judgmental", "lazy", "lonely", "lousy", "malicious",
                "mediocre", "messy", "miserable", "monotonous", "narrow-minded", "needy", "negative",
                "neglectful", "nervous", "noisy", "offensive", "overwhelmed", "painful", "pessimistic",
                "pointless", "poor", "reckless", "regretful", "resentful", "rigid", "rude", "sad",
                "selfish", "shallow", "shameful", "short-tempered", "slow", "stubborn", "stupid",
                "suspicious", "terrible", "threatening", "tiresome", "tragic", "ugly", "unbearable",
                "unfair", "unfriendly", "unhappy", "unhelpful", "unjust", "weak", "worthless"
            )

            for(word in arrayOfWords){
                if(positiveWord.contains(word)){
                    positiveCount++
                }else if(negativeWord.contains(word)){
                    negativeCount++
                }
            }

            val sentiment = when{
                positiveCount > negativeCount -> "Positive"
                negativeCount > positiveCount -> "Negative"
                else -> "Neutral"
            }

            withContext(Dispatchers.Main){
                dbHelper.onInsert(sentiment,sentence)
            }
        }
    }

}

