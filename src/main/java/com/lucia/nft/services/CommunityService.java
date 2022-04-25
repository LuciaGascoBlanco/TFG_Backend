package com.lucia.nft.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.util.List;

import com.lucia.nft.config.IPFS.IPFSConfig;
import com.lucia.nft.dto.ImageDto;
import com.lucia.nft.dto.MessageDto;
import com.lucia.nft.dto.UserDto;
import com.lucia.nft.dto.UserSummaryDto;
import com.lucia.nft.entities.Image;
import com.lucia.nft.entities.Message;
import com.lucia.nft.entities.Sold;
import com.lucia.nft.entities.User;
import com.lucia.nft.repositories.ImageRepository;
import com.lucia.nft.repositories.MessageRepository;
import com.lucia.nft.repositories.SoldRepository;
import com.lucia.nft.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multibase.binary.Base64;
import io.ipfs.multihash.Multihash;

@Service
public class CommunityService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final SoldRepository soldRepository;
    private String hash;

    @Autowired
    private IPFSConfig ipfsConfig;

    public CommunityService(MessageRepository messageRepository, UserRepository userRepository, ImageRepository imageRepository, SoldRepository soldRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.soldRepository = soldRepository;
    }

    //-------------------MESSAGES-----------------------//

    public List<MessageDto> getMessages(UserDto userDto) {
        User user = getUser(userDto);

        List<Message> messages = messageRepository.findMessages(user.getId());
        List<MessageDto> messageDtoList = new ArrayList<>();
        
        messages.forEach(message -> messageDtoList.add(new MessageDto(message.getId(), message.getContent(), new UserSummaryDto(message.getUser().getId(), message.getUser().getFirstName(), message.getUser().getLastName()), message.getCreatedDate())));

        return messageDtoList;
    }

    public MessageDto postMessage(UserDto userDto, MessageDto messageDto) {
        User user = getUser(userDto);

        Message message = new Message();
        message.setCreatedDate(LocalDateTime.now());
        message.setContent(messageDto.getContent());
        message.setUser(user);

        if (user.getMessages() == null) {
            user.setMessages(new ArrayList<>());
        }
        user.getMessages().add(message);

        Message savedMessage = messageRepository.save(message);

        return new MessageDto(savedMessage.getId(), savedMessage.getContent(), new UserSummaryDto(savedMessage.getUser().getId(), savedMessage.getUser().getFirstName(), savedMessage.getUser().getLastName()), savedMessage.getCreatedDate());
    }

    //-------------------IMAGES-----------------------//

    public List<ImageDto> getImages(UserDto userDto) throws IOException {
        User user = getUser(userDto);

        List<ImageDto> imageDtoList = new ArrayList<>();
        List<Image> images = imageRepository.findImages(user.getId());

        images.forEach(image -> {
            try{
                hash = image.getHash();

                IPFS ipfs = ipfsConfig.ipfs;
                Multihash filePointer = Multihash.fromBase58(hash);
                byte[] fileContents = ipfs.cat(filePointer);
                String encoded = Base64.encodeBase64String(fileContents);
           
                image.setPath(encoded);
                imageDtoList.add(new ImageDto(image.getId(), image.getTitle(), image.getPrice(), image.getPath(), image.getHash(), null, new UserSummaryDto(image.getUser().getId(), image.getUser().getFirstName(), image.getUser().getLastName()) , image.getCreatedDate()));

           } catch (IOException e) {}
        });
          
        return imageDtoList;
    }

    public ImageDto postImage(UserDto userDto, MultipartFile file, String title, String price) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(file.getBytes());
        IPFS ipfs = ipfsConfig.ipfs;
        NamedStreamable.InputStreamWrapper is = new NamedStreamable.InputStreamWrapper(inputStream);
        MerkleNode response = ipfs.add(is).get(0);
        System.out.println("Hash (base 58): " + response.hash.toBase58());

        String hash = response.hash.toBase58();

        User user = getUser(userDto);

        Image image = new Image();
        image.setId(user.getId());
        image.setTitle(title);
        image.setPrice(price);
        image.setUser(user);
        image.setPath(null);
        image.setHash(hash);
        image.setCreatedDate(LocalDateTime.now());

        if (user.getImages() == null) {
            user.setImages(new ArrayList<>());
        }
        user.getImages().add(image);
        userRepository.save(user);

        return new ImageDto(image.getId(), image.getTitle(), image.getPrice(), null, image.getHash(), null, new UserSummaryDto(image.getUser().getId(), image.getUser().getFirstName(), image.getUser().getLastName()) , image.getCreatedDate());      
    }

    //-------------------DELETE-----------------------//

    public List<ImageDto> deleteImage(UserDto userDto) throws IOException {
        User user = getUser(userDto);

        List<ImageDto> imageDtoList = new ArrayList<>();

        List<Image> images0 = imageRepository.findImages(user.getId());
        imageRepository.delete(images0.get(0));

        List<Image> images = imageRepository.findImages(user.getId());
        images.forEach(image -> {
            try{
                hash = image.getHash();

                IPFS ipfs = ipfsConfig.ipfs;
                Multihash filePointer = Multihash.fromBase58(hash);
                byte[] fileContents = ipfs.cat(filePointer);
                String encoded = Base64.encodeBase64String(fileContents);
           
                image.setPath(encoded);
                imageDtoList.add(new ImageDto(image.getId(), image.getTitle(), image.getPrice(), image.getPath(), image.getHash(), null, new UserSummaryDto(image.getUser().getId(), image.getUser().getFirstName(), image.getUser().getLastName()) , image.getCreatedDate()));

           } catch (IOException e) {}
        });
          
        return imageDtoList;
    } 

    public List<ImageDto> deleteImage2(UserDto userDto, String hash) throws IOException {
        User user = getUser(userDto);

        List<ImageDto> imageDtoList = new ArrayList<>();
        Image image0 = imageRepository.findImage(hash);
        imageRepository.delete(image0);

        List<Image> images = imageRepository.findImages(user.getId());
        images.forEach(image -> {
            try{
                IPFS ipfs = ipfsConfig.ipfs;
                Multihash filePointer = Multihash.fromBase58(hash);
                byte[] fileContents = ipfs.cat(filePointer);
                String encoded = Base64.encodeBase64String(fileContents);
           
                image.setPath(encoded);
                imageDtoList.add(new ImageDto(image.getId(), image.getTitle(), image.getPrice(), image.getPath(), image.getHash(), null, new UserSummaryDto(image.getUser().getId(), image.getUser().getFirstName(), image.getUser().getLastName()) , image.getCreatedDate()));

           } catch (IOException e) {}
        });
          
        return imageDtoList;
    } 

    //-------------------GALLERY-----------------------//

    public List<ImageDto> getGallery(UserDto userDto) throws IOException {
        List<Image> images = imageRepository.findAll();

        List<ImageDto> imageDtoList = new ArrayList<>();

        images.forEach(image -> {
            try{
                hash = image.getHash();

                IPFS ipfs = ipfsConfig.ipfs;
                Multihash filePointer = Multihash.fromBase58(hash);
                byte[] fileContents = ipfs.cat(filePointer);
                String encoded = Base64.encodeBase64String(fileContents);
           
                image.setPath(encoded);
                imageDtoList.add(new ImageDto(image.getId(), image.getTitle(), image.getPrice(), image.getPath(), image.getHash(), null, new UserSummaryDto(image.getUser().getId(), image.getUser().getFirstName(), image.getUser().getLastName()) , image.getCreatedDate()));

           } catch (IOException e) {}
        });
          
        return imageDtoList;
    }

    //-------------------SOLD-----------------------//
    
    public List<ImageDto> getSold(UserDto userDto) throws IOException {
        User user = getUser(userDto);
        Long id = user.getId();

        List<ImageDto> imageDtoList = new ArrayList<>();
        List<Sold> images = soldRepository.findSold(id);
        
        images.forEach(image -> {
            try{
                IPFS ipfs = ipfsConfig.ipfs;
                Multihash filePointer = Multihash.fromBase58(image.getHash());
                byte[] fileContents = ipfs.cat(filePointer);
                String encoded = Base64.encodeBase64String(fileContents);
           
                image.setPath(encoded);
                imageDtoList.add(new ImageDto(image.getId(), image.getTitle(), image.getPrice(), image.getPath(), image.getHash(), image.getLike(), new UserSummaryDto(image.getUser().getId(), image.getUser().getFirstName(), image.getUser().getLastName()) , image.getCreatedDate()));

           } catch (IOException e) {}
        });

        return imageDtoList;
    }
    
    public List<ImageDto> postSold(UserDto userDto, String hash) throws IOException {
        User user = getUser(userDto);
        Image image0 = imageRepository.findImage(hash);

        Sold sold = new Sold();
        sold.setId(user.getId());
        sold.setTitle(image0.getTitle());
        sold.setPrice(image0.getPrice());
        sold.setUser(user);
        sold.setPath(image0.getPath());
        sold.setHash(hash);
        sold.setLike("false");
        sold.setCreatedDate(image0.getCreatedDate());

        soldRepository.save(sold);

        List<ImageDto> imageDtoList = new ArrayList<>();
        return imageDtoList;
    }

    //-------------------FAVORITES-----------------------//

    public List<ImageDto> getLikes(UserDto userDto) throws IOException {
        User user = getUser(userDto);
        Long id = user.getId();

        List<ImageDto> imageDtoList = new ArrayList<>();
        List<Sold> images = soldRepository.findSold(id);
        
        images.forEach(image -> {

            if(image.getLike().equals("true")){
                try{
                    IPFS ipfs = ipfsConfig.ipfs;
                    Multihash filePointer = Multihash.fromBase58(image.getHash());
                    byte[] fileContents = ipfs.cat(filePointer);
                    String encoded = Base64.encodeBase64String(fileContents);
            
                    image.setPath(encoded);
                    imageDtoList.add(new ImageDto(image.getId(), image.getTitle(), image.getPrice(), image.getPath(), image.getHash(), image.getLike(), new UserSummaryDto(image.getUser().getId(), image.getUser().getFirstName(), image.getUser().getLastName()) , image.getCreatedDate()));

            } catch (IOException e) {}
        }
        });

        return imageDtoList;
    }
    
    public List<ImageDto> postLike(UserDto userDto, String hash) throws IOException {
        Sold soldImage = soldRepository.findImage(hash);
        soldImage.setLike("true");

        soldRepository.save(soldImage);

        List<ImageDto> imageDtoList = new ArrayList<>();
        return imageDtoList;
    }

    public List<ImageDto> postDislike(UserDto userDto, String hash) throws IOException {
        Sold soldImage = soldRepository.findImage(hash);
        soldImage.setLike("false");

        soldRepository.save(soldImage);

        List<ImageDto> imageDtoList = new ArrayList<>();
        return imageDtoList;
    }

    //-------------------USER-----------------------//

    public User getUser(UserDto userDto) {
        return userRepository.findById(userDto.getId()).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
