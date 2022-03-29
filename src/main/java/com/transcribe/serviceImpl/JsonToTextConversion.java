package com.transcribe.serviceImpl;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.transcribe.dto.AmazonTranscription;
import com.transcribe.dto.Item;
import com.transcribe.dto.Segment;
import com.transcribe.dto.SegmentItem;
@Service
public class JsonToTextConversion {
	public File JsonToReadableTextFile(AmazonTranscription amazonTranscription) throws JsonMappingException, IOException {
		PrintWriter writer = new PrintWriter("readable_json_data.txt");
		File file = new File("readable_json_data.txt");
		List<Item> items = amazonTranscription.getResults().getItems();
		List<Segment> segments = amazonTranscription.getResults().getSpeaker_labels().getSegments();
		for (Segment segment : segments) {
			StringBuilder builder = new StringBuilder();
			String content = "";
			builder.append(content);
			List<SegmentItem> segmentItems = segment.getItems();
			for (SegmentItem segmentItem : segmentItems) {
				String startTime2 = segmentItem.getStart_time();
				float floatStartTime2 = Float.parseFloat(startTime2);
				for (Item item : items) {
					String startTime = item.getStart_time();
					if (startTime != null) {
						float floatStartTime = Float.parseFloat(startTime);
						if (floatStartTime == floatStartTime2) {
							content += item.getAlternatives().get(0).getContent() + " ";
						}
					}
				}
			}
			writer.println("Speaker : " + segment.getSpeaker_label());
			writer.println("Content : " + content);
			writer.println();
		}
		writer.close();
		return file;
	}

}
