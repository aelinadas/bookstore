/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import neu.edu.applicationstats.Statistic;
import neu.edu.dao.BookDAO;
import neu.edu.pojo.Seller;
import neu.edu.utility.ValidateInputs;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author aelinadas
 */
public class ImageController extends AbstractController {

    private static final Logger logger = Logger.getLogger(ImageController.class);

    public ImageController() {
    }

    private Statistic statistic;

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Long beginTime = System.currentTimeMillis();
        ModelAndView mv = null;
        try {
            MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
            String action = multipartRequest.getParameter("action") != null ? multipartRequest.getParameter("action") : "";
            Seller seller = (Seller) multipartRequest.getSession(false).getAttribute("seller");
            AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
            String bucketName = new BookDAO().getBucketName();

            if (action.equalsIgnoreCase("add") && seller != null) {
                BookDAO bookDAO = new BookDAO();
                String isbn = multipartRequest.getParameter("isbn") != null ? multipartRequest.getParameter("isbn") : "";
                String title = multipartRequest.getParameter("title") != null ? multipartRequest.getParameter("title") : "";
                String authors = multipartRequest.getParameter("authors") != null ? multipartRequest.getParameter("authors") : "";
                String publicationDate = multipartRequest.getParameter("publicationDate") != null ? multipartRequest.getParameter("publicationDate") : "";
                String quantity = multipartRequest.getParameter("quantity") != null ? multipartRequest.getParameter("quantity") : "";
                String price = multipartRequest.getParameter("price") != null ? multipartRequest.getParameter("price") : "";
                String image = multipartRequest.getFile("image").getOriginalFilename();
                if (!isbn.equalsIgnoreCase("") && !title.equalsIgnoreCase("") && !authors.equalsIgnoreCase("") && !publicationDate.equalsIgnoreCase("") && !quantity.equalsIgnoreCase("") && !price.equalsIgnoreCase("")) {
                    ValidateInputs vi = new ValidateInputs();
                    if (vi.isISBN(isbn) && vi.isName(title) && vi.isAuthors(authors) && vi.isDate(publicationDate) && vi.isQuantity(quantity) && vi.isDecimal(price)) {
                        int result = bookDAO.addBook(isbn, title, authors, Date.valueOf(publicationDate), Integer.parseInt(quantity), Double.parseDouble(price), seller, image);
                        if (result != 0) {
                            MultipartFile multipartFile = multipartRequest.getFile("image");
                            String tempDir = System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename();
                            File file = new File(tempDir);
                            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
                            String extension = multipartFile.getOriginalFilename().split("[.]")[1];
                            String fileName = isbn + "-" + result + "-" + System.currentTimeMillis() + "." + extension;
                            PutObjectRequest putObj = new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicReadWrite);
                            ObjectMetadata metaData = new ObjectMetadata();
                            metaData.addUserMetadata("x-amz-meta-originalfilename", multipartFile.getOriginalFilename());
                            putObj.setMetadata(metaData);
                            s3client.putObject(putObj.withCannedAcl(CannedAccessControlList.PublicReadWrite));
                            file.delete();
                            logger.info("Book added");
                            statistic.getInstance().recordExecutionTimeToNow("S3AccessTimer", beginTime);
                            statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                            statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                            mv = new ModelAndView("seller-success", "message", "You have been successfully added a book.");
                        } else {
                            mv = new ModelAndView("seller-error", "message", "Not able to add Book");
                        }
                    } else {
                        System.out.println("else 2");
                        mv = new ModelAndView("seller-error", "message", "Not able to add Book");
                    }
                } else {
                    System.out.println("else 3");
                    mv = new ModelAndView("retailer-error", "message", "Not able to add Product");
                }
            }
            if (action.equals("addMoreImg") && seller != null) {
                String isbn = multipartRequest.getParameter("isbn") != null ? multipartRequest.getParameter("isbn") : "";
                String bookID = multipartRequest.getParameter("bookID") != null ? multipartRequest.getParameter("bookID") : "";
                if (!isbn.equalsIgnoreCase("") && !bookID.equalsIgnoreCase("")) {
                    MultipartFile multipartFile = multipartRequest.getFile("image");
                    String tempDir = System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename();
                    File file = new File(tempDir);
                    FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
                    String extension = multipartFile.getOriginalFilename().split("[.]")[1];
                    String fileName = isbn + "-" + bookID + "-" + System.currentTimeMillis() + "." + extension;
                    Map<String, String> userMetadata = new HashMap<String, String>();
                    userMetadata.put("x-amz-meta-originalfilename", multipartFile.getOriginalFilename());
                    ObjectMetadata metaData = new ObjectMetadata();
                    metaData.setUserMetadata(userMetadata);
                    PutObjectRequest putObj = new PutObjectRequest(bucketName, fileName, file);
                    putObj.setMetadata(metaData);
                    putObj.withCannedAcl(CannedAccessControlList.PublicReadWrite);
                    s3client.putObject(putObj);
                    file.delete();
                    BookDAO bdao = new BookDAO();
                    int result = bdao.updateImage(Integer.parseInt(bookID), multipartFile.getOriginalFilename());
                    logger.info("Image added");
                    if (result == 1) {
                        statistic.getInstance().recordExecutionTimeToNow("S3AccessTimer", beginTime);
                        statistic.getInstance().recordExecutionTimeToNow("DataBaseAcessTimer", beginTime);
                        statistic.getInstance().recordExecutionTimeToNow("WebRequestTimer", beginTime);

                        mv = new ModelAndView("seller-success", "message", "Image successfully added.");
                    } else {
                        mv = new ModelAndView("seller-error", "message", "Image not added");
                    }
                }
            }
        } catch (Exception ex) {
            mv = new ModelAndView("seller-error", "message", "Image not added");
        }
        return mv;
    }
}
