package flash.model;

public class VocabModel {

	private int vocabID;
	private String word;
	private String meaning;
	private String expo;

	public VocabModel(int vocabID, String word, String meaning, String expo) {

		this.vocabID = vocabID;
		this.word = word;
		this.meaning = meaning;
		this.expo = expo;

	}

	public VocabModel(int vocabID, String word, String meaning) {
		this.vocabID = vocabID;
		this.word = word;
		this.meaning = meaning;
	}

	public int getVocabID() {
		return vocabID;
	}

	public String getWord() {
		return word;
	}

	public String getMeaning() {
		return meaning;
	}

	public String getExpo() {
		return expo;
	}

}
