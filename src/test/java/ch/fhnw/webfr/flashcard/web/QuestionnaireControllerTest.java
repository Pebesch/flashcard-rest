package ch.fhnw.webfr.flashcard.web;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ch.fhnw.webfr.flashcard.TestUtil;
import ch.fhnw.webfr.flashcard.TestUtil.QuestionnaireBuilder;
import ch.fhnw.webfr.flashcard.domain.Questionnaire;
import ch.fhnw.webfr.flashcard.persistence.QuestionnaireRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(QuestionnaireController.class)
public class QuestionnaireControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private QuestionnaireRepository questionnaireRepositoryMock;

	@Before
	public void setUp() {
		Mockito.reset(questionnaireRepositoryMock);
	}

	@Test
	public void testFindAll() throws Exception {
		List<Questionnaire> qs = new ArrayList<Questionnaire>();
		qs.add(new QuestionnaireBuilder("1").description("Description1").title("Title1").build());
		qs.add(new QuestionnaireBuilder("2").description("Description2").title("Title2").build());

		when(questionnaireRepositoryMock.findAll()).thenReturn(qs);

		mockMvc.perform(get("/questionnaires")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(qs)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is("1")))
				.andExpect(jsonPath("$[0].title", is("Title1")))
				.andExpect(jsonPath("$[0].description", is("Description1")))
				.andExpect(jsonPath("$[1].id", is("2")))
				.andExpect(jsonPath("$[1].title", is("Title2")))
				.andExpect(jsonPath("$[1].description", is("Description2")))
				.andExpect(jsonPath("$.length()", is(2)));

		Mockito.verify(questionnaireRepositoryMock, times(1)).findAll();
	}

	@Test
	public void testFindById() throws Exception {
		Questionnaire q = new QuestionnaireBuilder("1").description("Description1").title("Title1").build();

		when(questionnaireRepositoryMock.existsById("1")).thenReturn(true);
		when(questionnaireRepositoryMock.findById("1")).thenReturn(Optional.of(q));

		mockMvc.perform(get("/questionnaires/{id}", "1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(TestUtil.convertObjectToJsonBytes(q)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is("1")))
			.andExpect(jsonPath("$.title", is("Title1")))
			.andExpect(jsonPath("$.description", is("Description1")));

		Mockito.verify(questionnaireRepositoryMock, times(1)).existsById("1");
		Mockito.verify(questionnaireRepositoryMock, times(1)).findById("1");
	}

	@Test
	public void testFindByIdNotFound() throws Exception {
		when(questionnaireRepositoryMock.existsById("1")).thenReturn(false);

		mockMvc.perform(get("/questionnaires/{id}", "1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(""))
			.andExpect(status().isNotFound());

		Mockito.verify(questionnaireRepositoryMock, times(1)).existsById("1");
		Mockito.verify(questionnaireRepositoryMock, times(0)).findById("1");
	}
}
