package jp.qst.demo.been;


/**
 * テンプレートマッチ処理時返却用DTO
 * @author Hisashi
 *
 */
public class TemplateMatchDto extends ImageProcessingDefaultDto {
	private boolean isMatch = false;
	
	public boolean getIsMatch() {
		return isMatch;
	}
	public void setIsMatch(boolean isMatch) {
		this.isMatch = isMatch;
	}
}
