package com.scrapper.app.controller;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.scrapper.app.model.Item;
import com.scrapper.app.model.Movie;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(value = "/scrape")
public class WebScrapperController {

	@RequestMapping(value = "/imdb")
	public ResponseEntity<List<Movie>> webScrapper() throws IOException {
		Document document = Jsoup.connect("https://www.imdb.com/chart/top/").get();
		// System.out.println(document.outerHtml());
		List<Movie> movieList = new ArrayList<>();
		for (Element row : document.select("table.chart.full-width tr")) {
			final String title = row.select(".titleColumn a").text();
			final String rating = row.select(".imdbRating").text();
			movieList.add(new Movie(title, rating));
		}
		return new ResponseEntity<>(movieList, HttpStatus.OK);
	}

	@RequestMapping(value = "craigslist")
	public ResponseEntity<List<Item>> scrapeCraigslist() {
		String searchQuery = "Iphone 6s";
		List<Item> itemsList = new ArrayList<>();
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		try {
			String searchUrl = "https://newyork.craigslist.org/search/sss?sort=rel&query="
					+ URLEncoder.encode(searchQuery, "UTF-8");
			HtmlPage page = client.getPage(searchUrl);
			List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//li[@class='result-row']");
			for (HtmlElement htmlItem : items) {
				HtmlElement itemAnchor = ((HtmlElement) htmlItem
						.getFirstByXPath(".//div/h3[@class='result-heading']/a"));

				HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//a/span[@class='result-price']"));

				String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();
				Item item = new Item(itemAnchor.asText(), new BigDecimal(itemPrice.replace("$", "")));
				itemsList.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(itemsList, HttpStatus.OK);
	}

}
