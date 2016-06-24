package jp.qst.demo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import jp.qst.demo.been.TemplateMatchDto;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.google.gson.Gson;

/**
 * Servlet implementation class MorphologicalAnalysis
 */
@WebServlet("/TemplateMatch")
@MultipartConfig()
public class TemplateMatch extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TemplateMatch() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		// ���͉摜��ۑ�
		Part part = request.getPart("inputImage");
		String inputImageName = this.getFileName(part);
		part.write(getServletContext().getRealPath("/WEB-INF/uploaded") + "/"
				+ inputImageName);

		//�@�e���v���[�g�摜��ۑ�
		Part partTmp = request.getPart("templateImage");
		String templateImageName = this.getFileName(partTmp);
		partTmp.write(getServletContext().getRealPath("/WEB-INF/uploaded") + "/"
				+ templateImageName);

		String dir = getServletContext().getRealPath("/WEB-INF/uploaded");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// ���͉摜�̓ǂݍ���
		Mat inputImg = Imgcodecs.imread(dir + "/" + inputImageName);
		// �e���v���[�g�摜�̓ǂݍ���
		Mat tmpImg = Imgcodecs.imread(dir + "/" + templateImageName);

		Mat result = new Mat();

		// �e���v���[�g�}�b�`���O
		Imgproc.matchTemplate(inputImg, tmpImg, result,
				Imgproc.TM_CCOEFF_NORMED);
		// ����
		Imgproc.threshold(result, result, 0.8, 1.0, Imgproc.THRESH_TOZERO);
		// �e���v���[�g�摜�ƃ}�b�`����摜����������
		boolean matchImage = false;
		// �e���v���[�g�摜�̕��������摜�ɐԐF�̋�`�ň͂�
		for (int i = 0; i < result.rows(); i++) {
			for (int j = 0; j < result.cols(); j++) {
				if (result.get(i, j)[0] > 0) {
					// Imgproc.rectangle(inputImg, new Point(j, i), new Point(j
					// + tmpImg.cols(), i + tmpImg.rows()), new Scalar(0,
					// 0, 255));
					matchImage = true;
				}
			}
		}

		System.out.println("matchImage:" + matchImage);

		// �ԋp�l�̐ݒ�
		Gson gson = new Gson();
		TemplateMatchDto outputData = new TemplateMatchDto();
		outputData.setStatusCd("200");
		outputData.setIsMatch(matchImage);

		// ���X�|���X
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(gson.toJson(outputData));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

    private String getFileName(Part part) {
        String name = null;
        for (String dispotion : part.getHeader("Content-Disposition").split(";")) {
            if (dispotion.trim().startsWith("filename")) {
                name = dispotion.substring(dispotion.indexOf("=") + 1).replace("\"", "").trim();
                name = name.substring(name.lastIndexOf("\\") + 1);
                break;
            }
        }
        return name;
    }
}
