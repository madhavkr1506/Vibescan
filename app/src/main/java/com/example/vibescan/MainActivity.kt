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
                "accept", "admire", "admir", "agree", "amaze", "amazing", "appreciate",
                "attract", "awesome", "believe", "calm", "care", "cheer", "clean",
                "comfort", "enjoy", "excite", "exciting", "friend", "friendly", "funny",
                "generous", "gentle", "genuine", "great", "happy", "help", "honest",
                "hope", "hopeful", "inspire", "joy", "kind", "love", "loyal", "neat",
                "nice", "optimist", "optimistic", "perfect", "pleasant", "polite",
                "proud", "relax", "respect", "satisfy", "share", "smart", "strong",
                "support", "sweet", "thankful", "trust", "understand", "wonder",
                "wonderful"
            )
            val negativeWord = listOf(
                "abuse", "angry", "annoy", "argue", "bad", "bore", "boring", "break",
                "cheat", "complain", "confuse", "confusing", "cry", "damage", "dirty",
                "disappoint", "disappointing", "disrespect", "fail", "fear", "fight",
                "forget", "guilt", "guilty", "hate", "hurt", "jealous", "lazy", "lie",
                "lonely", "lose", "mean", "mess", "messy", "mistake", "neglect",
                "nervous", "noisy", "rude", "sad", "selfish", "shout", "sick", "slow",
                "stress", "stupid", "suspect", "terrible", "tire", "tired", "ugly",
                "unfair", "unhappy", "upset", "weak", "worthless", "wrong"
            )


            val lemmatizationList : ArrayList<String> = arrayListOf()
            for(word in arrayOfWords){
                if(word.endsWith("ing") && word.length > 4){
                    lemmatizationList.add(word.substring(0,word.length - 3).lowercase())
                }
                else if(word.endsWith("es") && word.length > 3){
                    lemmatizationList.add(word.substring(0,word.length - 2).lowercase())
                }else if(word.endsWith("s") && word.length > 2){
                    lemmatizationList.add(word.substring(0,word.length - 1).lowercase())
                }
                else{
                    lemmatizationList.add(word.lowercase())
                }
            }

            for(word in lemmatizationList){
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

