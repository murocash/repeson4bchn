package cash.muro.bch.rpc.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient.Version;
import java.time.Duration;

import com.emiperez.repeson.client.JsonRpcVersion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BchnClientProperties {
	
	private URI uri;
	private String userName = "";
	private String password = "";
	private Version httpVersion = Version.HTTP_1_1;
	private JsonRpcVersion jsonRpcVersion = JsonRpcVersion.v1_1;
	private Duration timeout = Duration.ofSeconds(5);
	private String contentType = "plain/text";
	
	public void setUri(String uriStr) throws URISyntaxException {
		this.uri = new URI(uriStr.trim());
	}
	
	
}
