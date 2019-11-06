package lab.microservice.labservice.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import lab.microservice.labservice.business.BusinessBase;
import lab.microservice.labservice.exception.RetornoNegocioException;
import lab.microservice.labservice.model.EntityBase;
import lab.microservice.labservice.model.util.JsonReturn;
import lab.microservice.labservice.model.util.ParValor;
import lab.microservice.labservice.model.util.ParametroPesquisa;
import lab.microservice.labservice.util.ModelUtils;

@SuppressWarnings({ "static-access", "rawtypes" })
public abstract class ServiceBase<T extends EntityBase> implements Serializable {

	private static final long serialVersionUID = -2109810374032274822L;
	private final String erroGET = "get method not allowed";

	public abstract BusinessBase<T> getBusiness();

	@PostMapping("/contains")
	public ResponseEntity existeObjetoPorAtributos(@RequestBody ParametroPesquisa pesquisa) {
		try {
			HashMap<String, Object> atributos = getAtributos(pesquisa);

			boolean existe;

			existe = getBusiness().existeObjetoPorAtributos(atributos);

			return ResponseEntity.ok().body(existe);
		} catch (Exception e) {
			return ResponseEntity.ok(RetornoNegocioException.builder()
					.erro("Erro ao realizar consulta existeObjetoPorAtributos").exception(e.getMessage()).build())
					.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// @JWTTokenNeeded
	@GetMapping("/{code}")
	public ResponseEntity getById(@PathVariable long code, @RequestHeader("Authorization") String auth) {
		try {
			T obj = getBusiness().getObjeto(code);
			if (obj != null)
				return ResponseEntity.ok().body(obj);
			else
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
		} catch (Exception e) {
			return ResponseEntity.ok(RetornoNegocioException.builder().erro("Error trying to get object by Id")
					.exception(e.getMessage()).build()).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/list")
	public ResponseEntity getListaPorAtributos(ParametroPesquisa pesquisa) {
		try {
			String ordenacao = getOrdenacao(pesquisa);
			HashMap<String, Object> atributos = getAtributos(pesquisa);

			List<T> lista;

			lista = getBusiness().getListaPorAtributos(atributos, ordenacao);

			return ResponseEntity.ok(lista);
		} catch (Exception e) {
			return setError("Error getting objects", e.getMessage());
		}
	}

	// @JWTTokenNeeded
	@PostMapping("/get")
	public ResponseEntity getObjetoPorAtributos(ParametroPesquisa pesquisa,
			@RequestHeader("Authorization") String auth) {
		try {
			HashMap<String, Object> atributos = getAtributos(pesquisa);
			Long id = contemId(atributos);

			T entidade;

			if (id != null)
				entidade = getBusiness().getObjeto(id);
			else
				entidade = getBusiness().getObjetoPorAtributos(atributos);

			return ResponseEntity.ok(entidade);
		} catch (Exception e) {
			return setError("Error getting objects", e.getMessage());
		}
	}

	@PostMapping("/all")
	public ResponseEntity getListaTodos(ParametroPesquisa pesquisa) {
		try {
			String ordenacao = getOrdenacao(pesquisa);

			List<T> lista;

			if (ModelUtils.isNullEmpty(ordenacao))
				lista = getBusiness().getListaTodos();
			else
				lista = getBusiness().getListaTodos(ordenacao);

			return ResponseEntity.ok(lista);
		} catch (Exception e) {
			return ResponseEntity.ok(RetornoNegocioException.builder().erro("Erro ao realizar consulta getListaTodos")
					.exception(e.getMessage()).build()).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/save")
	public ResponseEntity save(T entidade) {
		try {
			getBusiness().salvar(entidade);
			return ResponseEntity.ok(entidade);
		} catch (Exception e) {
			return ResponseEntity.ok(
					RetornoNegocioException.builder().erro("Erro ao salvar registro").exception(e.getMessage()).build())
					.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity delete(T entidade) {
		try {
			long id = entidade.getId();
			getBusiness().remover(entidade.getId());
			return ResponseEntity.ok(JsonReturn.builder().message("Registro " + id + " removido com sucesso").build());
		} catch (Exception e) {
			return ResponseEntity.ok(RetornoNegocioException.builder().erro("Erro ao remover registro")
					.exception(e.getMessage()).build()).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	protected ResponseEntity setError(String error, String ex, String tipo) {
		return ResponseEntity.ok(RetornoNegocioException.builder().erro(error).exception(ex).tipo(tipo).build())
				.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	protected ResponseEntity setError(String error, String ex) {
		return ResponseEntity.ok(RetornoNegocioException.builder().erro(error).exception(ex).build())
				.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	private Long contemId(HashMap<String, Object> atributos) {

		try {
			if (atributos.containsKey("id"))
				return Long.parseLong(atributos.get("id").toString());
		} catch (Exception e) {
			return null;
		}

		return null;
	}

	private HashMap<String, Object> getAtributos(ParametroPesquisa pesquisa) {
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		if (pesquisa != null && pesquisa.getListaPares() != null) {
			for (ParValor par : pesquisa.getListaPares()) {
				mapa.put(par.getChave(), par.getValor());
			}
		}
		return mapa;
	}

	private String getOrdenacao(ParametroPesquisa pesquisa) {
		if (pesquisa != null && !ModelUtils.isNullEmpty(pesquisa.getOrdenacao()))
			return pesquisa.getOrdenacao();

		return null;
	}

	protected ResponseEntity getError() {
		return ResponseEntity.ok(RetornoNegocioException.builder().erro(erroGET).exception(erroGET).build())
				.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}