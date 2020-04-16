package gri.engine.service;

import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gri.engine.util.Constant;

public class IndexService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);

	public void createIndex(String cacheUUID) {
		IndexWriter writer = null;
		try {
			Directory directory = FSDirectory.open(new File(Constant.CacheFolder));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,
					new StandardAnalyzer(Version.LUCENE_36));
			writer = new IndexWriter(directory, config);
			Document doc = null;
			File file = new File(Constant.CacheFolder);
			for (File f : file.listFiles()) {
				if (f.getName().equals(cacheUUID + ".txt")) {
					doc = new Document();
					doc.add(new Field("content", new FileReader(f)));
					doc.add(new Field("cacheUUID", cacheUUID, Field.Store.YES, Field.Index.NOT_ANALYZED));
					writer.addDocument(doc);
					LOGGER.info("add index: [cache UUID:" + cacheUUID + "]");
					break;
				}
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void delelteIndex(String cacheUUID) {
		IndexWriter writer = null;
		try {
			Directory directory = FSDirectory.open(new File(Constant.CacheFolder));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,
					new StandardAnalyzer(Version.LUCENE_36));
		
			writer = new IndexWriter(directory, config);
			
			QueryParser parser = new QueryParser(Version.LUCENE_36, "cacheUUID",
					new StandardAnalyzer(Version.LUCENE_36));
			Query query = parser.parse(cacheUUID);
			writer.deleteDocuments(query);
			LOGGER.info("delete index: [cache UUID:" + cacheUUID + "]");
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void delelteIndex() {
		IndexWriter writer = null;
		try {
			Directory directory = FSDirectory.open(new File(Constant.CacheFolder));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,
					new StandardAnalyzer(Version.LUCENE_36));
			writer = new IndexWriter(directory, config);
			writer.deleteAll();
			LOGGER.info("delete all index");
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void createIndex() {
		IndexWriter writer = null;
		try {
			Directory directory = FSDirectory.open(new File(Constant.CacheFolder));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,
					new StandardAnalyzer(Version.LUCENE_36));
			writer = new IndexWriter(directory, config);
			Document doc = null;
			File dir = new File(Constant.CacheFolder);
			for (File file : dir.listFiles()) {
				String name = file.getName();
				String cacheUUID = "";
				if (name.endsWith(".txt")) {
					int index = name.lastIndexOf('.');
					cacheUUID = name.substring(0, index);
					if (cacheUUID.length() < 36)
						continue;
					doc = new Document();
					doc.add(new Field("content", new FileReader(file)));
					doc.add(new Field("cacheUUID", cacheUUID, Field.Store.YES, Field.Index.NOT_ANALYZED));
					writer.addDocument(doc);
					LOGGER.info("add index: [cache UUID:" + cacheUUID + "]");
					break;
				} 
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<String> search(String value) {
		LOGGER.info("begin search file which content contain \"" + value + "\"...");
		List<String> cacheUUIDs = new ArrayList<String>();
		IndexReader reader = null;
		IndexSearcher searcher = null;
		try {
			Directory directory = FSDirectory.open(new File(Constant.CacheFolder));
			reader = IndexReader.open(directory);
			searcher = new IndexSearcher(reader);
			QueryParser parser = new QueryParser(Version.LUCENE_36, "content", new StandardAnalyzer(Version.LUCENE_36));
			Query query = parser.parse(value);
			TopDocs tds = searcher.search(query, 10);
			ScoreDoc[] sds = tds.scoreDocs;
			for (ScoreDoc sd : sds) {
				Document doc = searcher.doc(sd.doc);
				LOGGER.info("content:" + doc.get("content") + ", cache UUID:" + doc.get("cacheUUID"));
				cacheUUIDs.add(doc.get("cacheUUID"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (searcher != null) {
				try {
					searcher.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return cacheUUIDs;

	}

	public static void main(String[] args) {

		IndexService is = new IndexService();
		is.delelteIndex();
		is.createIndex();
		is.search("GriDoc");
	}

}
