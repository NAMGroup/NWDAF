package io.nwdaf.analytics.model;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.annotations.ApiModel;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This represents the identifier of the N3IWF ID as specified in clause 9.3.1.57 of 3GPP TS 38.413 in hexadecimal representation. Each character in the string shall take a value of \&quot;0\&quot; to \&quot;9\&quot;, \&quot;a\&quot; to \&quot;f\&quot; or \&quot;A\&quot; to \&quot;F\&quot; and shall represent 4 bits. The most significant character representing the 4 most significant bits of the N3IWF ID shall appear first in the string, and the character representing the 4 least significant bit of the N3IWF ID shall appear last in the string.
 */
@ApiModel(description = "This represents the identifier of the N3IWF ID as specified in clause 9.3.1.57 of 3GPP TS 38.413 in hexadecimal representation. Each character in the string shall take a value of \"0\" to \"9\", \"a\" to \"f\" or \"A\" to \"F\" and shall represent 4 bits. The most significant character representing the 4 most significant bits of the N3IWF ID shall appear first in the string, and the character representing the 4 least significant bit of the N3IWF ID shall appear last in the string.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-01-27T12:24:16.086762+02:00[Europe/Athens]")
public class N3IwfId   {

	private String n3IwfId = null;
	
	public N3IwfId(String n3IwfId ) {
		  String pattern = "^[A-Fa-f0-9]+$";
		  Pattern r = Pattern.compile(pattern);
		  Matcher m = r.matcher(n3IwfId );
		  if (m.matches()) {
			  this.n3IwfId  = n3IwfId ;
		}
		  else {
			  throw new IllegalArgumentException("Not valid n3IwfId  argument. N3IwfId  must must follow the \"^[A-Fa-f0-9]+$\" pattern.");
		  }
	}
  
	public String n3IwfIdValue() {
		return this.n3IwfId ;
	}
	
	
	
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class N3IwfId {\n");
    
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
